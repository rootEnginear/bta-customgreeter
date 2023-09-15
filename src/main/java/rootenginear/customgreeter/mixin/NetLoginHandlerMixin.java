package rootenginear.customgreeter.mixin;

import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet3Chat;
import net.minecraft.server.net.handler.NetLoginHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import rootenginear.customgreeter.CustomGreeter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(value = {NetLoginHandler.class}, remap = false)
public class NetLoginHandlerMixin {
    @ModifyArg(
            method = "doLogin(Lnet/minecraft/core/net/packet/Packet1Login;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/net/ServerConfigurationManager;sendPacketToAllPlayers(Lnet/minecraft/core/net/packet/Packet;)V",
                    ordinal = 0
            ),
            index = 0
    )
    private Packet newWelcome(Packet packet) {
        if (packet instanceof Packet3Chat) {
            final String str = ((Packet3Chat) packet).message;
            Pattern namePattern = Pattern.compile("§4(.+)§4 joined the game\\.");
            Matcher nameMatch = namePattern.matcher(str);
            if (nameMatch.matches()) {
                final String name = nameMatch.group(1);
                return new Packet3Chat(CustomGreeter.WELCOME_STR.replace("{PLAYER}", name));
            }
            return packet;
        }
        return packet;
    }
}
