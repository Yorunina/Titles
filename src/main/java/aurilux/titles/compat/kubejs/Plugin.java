package aurilux.titles.compat.kubejs;

import aurilux.titles.api.Title;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.compat.kubejs.events.TitleRegistryEventJS;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.script.BindingsEvent;

public class Plugin extends KubeJSPlugin {

    public static EventGroup TitleGroup = EventGroup.of("TitlesEvent");
    public static EventHandler TITLE_REGISTRY = TitleGroup
            .server("onTitleRegistry", () -> TitleRegistryEventJS.class);

    @Override
    public void registerEvents() {
        TitleGroup.register();
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("TitleManager", TitleManager.class);
        event.add("TitleAwardType", Title.AwardType.class);
    }
}
