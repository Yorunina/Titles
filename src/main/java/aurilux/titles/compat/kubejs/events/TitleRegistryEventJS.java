package aurilux.titles.compat.kubejs.events;

import aurilux.titles.api.Title;
import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class TitleRegistryEventJS extends EventJS {
    public List<Title> titles = new ArrayList<>();
    public TitleRegistryEventJS() {}

    public void addTitle(Title title) {
        this.titles.add(title);
    }
    public List<Title> getTitles() {
        return titles;
    }
    public Title.Builder create(ResourceLocation id) {
        return Title.Builder.create(id.getNamespace()).id(id);
    }
}
