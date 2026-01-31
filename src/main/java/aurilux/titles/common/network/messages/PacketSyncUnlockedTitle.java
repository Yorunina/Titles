package aurilux.titles.common.network.messages;

import aurilux.titles.common.core.TitleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncUnlockedTitle {
    private final ResourceLocation titleKey;
    private final long gameTime;

    public PacketSyncUnlockedTitle(ResourceLocation titleKey, long gameTime) {
        this.titleKey = titleKey;
        this.gameTime = gameTime;
    }

    public static void encode(PacketSyncUnlockedTitle msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.titleKey.toString());
        buf.writeLong(msg.gameTime);
    }

    public static PacketSyncUnlockedTitle decode(FriendlyByteBuf buf) {
        return new PacketSyncUnlockedTitle(new ResourceLocation(buf.readUtf()), buf.readLong());
    }

    public static void handle(PacketSyncUnlockedTitle msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            @Override
            public void run() {
                Player player = Minecraft.getInstance().player;
                if (player != null) {
                    TitleManager.doIfPresent(player, cap ->
                            cap.add(TitleManager.getTitle(msg.titleKey), msg.gameTime));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}