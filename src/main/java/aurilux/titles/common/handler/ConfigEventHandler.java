package aurilux.titles.common.handler;

import aurilux.titles.common.core.TitleManager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ConfigEventHandler {
    public static void onTabListNameFormat(PlayerEvent.TabListNameFormat event) {
        Player player = event.getEntity();
        TitleManager.doIfPresent(player, cap -> {
            event.setDisplayName(TitleManager.getFormattedDisplayName(cap.getDisplayTitle(), player, cap));
        });
    }
}
