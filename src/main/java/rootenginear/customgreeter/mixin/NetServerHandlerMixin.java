package rootenginear.customgreeter.mixin;

import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet3Chat;
import net.minecraft.core.net.packet.Packet61PlaySoundEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.net.handler.NetServerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rootenginear.customgreeter.CustomGreeter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(value = {NetServerHandler.class}, remap = false)
public class NetServerHandlerMixin {
    @Shadow
    private MinecraftServer mcServer;

    @ModifyArg(
            method = "handleErrorMessage(Ljava/lang/String;[Ljava/lang/Object;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/net/ServerConfigurationManager;sendPacketToAllPlayers(Lnet/minecraft/core/net/packet/Packet;)V",
                    ordinal = 0
            ),
            index = 0
    )
    private Packet newFarewell(Packet packet) {
        if (packet instanceof Packet3Chat) {
            final String str = ((Packet3Chat) packet).message;
            Pattern namePattern = Pattern.compile("(.+)§4 left the game\\.");
            Matcher nameMatch = namePattern.matcher(str);
            if (nameMatch.matches()) {
                final String name = nameMatch.group(1);
                return new Packet3Chat(CustomGreeter.FAREWELL_STR.replace("{PLAYER}", name));
            }
            return packet;
        }
        return packet;
    }

    @ModifyArg(
            method = "kickPlayer(Ljava/lang/String;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/net/ServerConfigurationManager;sendPacketToAllPlayers(Lnet/minecraft/core/net/packet/Packet;)V",
                    ordinal = 0
            ),
            index = 0
    )
    private Packet newKick(Packet packet) {
        if (packet instanceof Packet3Chat) {
            final String str = ((Packet3Chat) packet).message;
            Pattern namePattern = Pattern.compile("(.+)§4 was kicked from the game\\.");
            Matcher nameMatch = namePattern.matcher(str);
            if (nameMatch.matches()) {
                final String name = nameMatch.group(1);
                return new Packet3Chat(CustomGreeter.KICK_STR.replace("{PLAYER}", name));
            }
            return packet;
        }
        return packet;
    }

    @Inject(
            method = "handleErrorMessage(Ljava/lang/String;[Ljava/lang/Object;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/net/ServerConfigurationManager;sendPacketToAllPlayers(Lnet/minecraft/core/net/packet/Packet;)V",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            )
    )
    private void logoutPing(String s, Object[] aobj, CallbackInfo ci) {
        this.mcServer.configManager.sendPacketToAllPlayers(new Packet61PlaySoundEffect(CustomGreeter.FAREWELL_SOUND, 0, 0, 0, -1));
    }
}
