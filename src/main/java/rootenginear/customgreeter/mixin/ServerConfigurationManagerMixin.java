package rootenginear.customgreeter.mixin;

import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet61PlaySoundEffect;
import net.minecraft.server.entity.player.EntityPlayerMP;
import net.minecraft.server.net.ServerConfigurationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = {ServerConfigurationManager.class}, remap = false)
public class ServerConfigurationManagerMixin {
    @Shadow
    public List<EntityPlayerMP> playerEntities;

    @Inject(method = "sendPacketToAllPlayers", at = @At("HEAD"), cancellable = true)
    public void sendPacketToAllPlayersLocation(Packet packet, CallbackInfo ci) {
        if (packet instanceof Packet61PlaySoundEffect && ((Packet61PlaySoundEffect) packet).data == -1) {
            for (EntityPlayerMP playerEntity : this.playerEntities) {
                int x = (int) Math.round(playerEntity.x);
                int y = (int) Math.round(playerEntity.y);
                int z = (int) Math.round(playerEntity.z);
                playerEntity.playerNetServerHandler.sendPacket(new Packet61PlaySoundEffect(((Packet61PlaySoundEffect) packet).soundID, x, y + 10, z, ((Packet61PlaySoundEffect) packet).data));
            }
            ci.cancel();
        }
    }
}
