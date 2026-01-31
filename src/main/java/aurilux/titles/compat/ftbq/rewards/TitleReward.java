package aurilux.titles.compat.ftbq.rewards;

import aurilux.titles.api.Title;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.core.TitleRegistry;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.reward.Reward;
import dev.ftb.mods.ftbquests.quest.reward.RewardType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class TitleReward extends Reward {
    private ResourceLocation titleId;
    private Title title;

    public TitleReward(long id, Quest quest) {
        super(id, quest);
        this.titleId = Title.NULL_TITLE.getID();
    }

    public RewardType getType() {
        return FTBRewardTypes.TITLE;
    }

    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putString("titleId", this.titleId.toString());
    }

    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.titleId = new ResourceLocation(nbt.getString("titleId"));
        this.title = TitleManager.getTitle(this.titleId);
    }

    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeResourceLocation(this.titleId);
    }

    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.titleId = buffer.readResourceLocation();
        this.title = TitleManager.getTitle(this.titleId);
    }

    @OnlyIn(Dist.CLIENT)
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        Map<ResourceLocation, Title> titleMap = new HashMap<>();
        TitleRegistry.get().getTitles().values().forEach(titleMap::putAll);

        config.addEnum("titleId", this.titleId, (v) -> {
            this.titleId = v;
        }, NameMap.of(Title.NULL_TITLE.getID(), titleMap.keySet().stream().toList())
                .nameKey((v) -> titleMap.getOrDefault(v, Title.NULL_TITLE).getDefaultDisplay()).icon((v) -> Icons.CHAT).create(), Title.NULL_TITLE.getID());
    }

    public void claim(ServerPlayer player, boolean notify) {
        TitleManager.unlockTitle(player, this.titleId);
    }

    @OnlyIn(Dist.CLIENT)
    public MutableComponent getAltTitle() {
        return Component.translatable("ftbquests.reward.titles.title").append(": ").append(this.title.getTextComponent(true));
    }
}