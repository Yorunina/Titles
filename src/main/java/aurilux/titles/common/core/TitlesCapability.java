package aurilux.titles.common.core;

import aurilux.titles.api.Title;
import aurilux.titles.common.TitlesMod;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TitlesCapability {
    public static final Capability<TitlesCapability> TITLES_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final ResourceLocation NAME = TitlesMod.prefix("titles");

    private final String GENDER_SETTING = "gender_setting";
    private final String DISPLAY_TITLE = "display_title";
    private final String OBTAINED_TITLES = "obtained_titles";
    private final String NICKNAME = "nickname";
    private final String TITLE_OBTAIN_TIMES = "title_obtain_times";
    private final Set<Title> obtainedTitles = new HashSet<>();
    private final HashMap<String, TimeData> titleObtainTimes = new HashMap<>();

    private String nickname = "";
    private Title displayTitle = Title.NULL_TITLE;
    private boolean genderSetting = true;

    public boolean add(Title title, long levelTime) {
        if (!title.isNull() && obtainedTitles.add(title)) {
            titleObtainTimes.put(title.getID().toString(), TimeData.now(levelTime));
            return true;
        }
        return false;
    }


    public void remove(Title title) {
        obtainedTitles.remove(title);
    }

    public void setNickname(String newNickname) {
        nickname = newNickname;
    }

    public String getNickname() {
        return nickname;
    }

    public Set<Title> getObtainedTitles() {
        return obtainedTitles;
    }

    public TimeData getObtainTime(ResourceLocation titleId) {
        return titleObtainTimes.get(titleId.toString());
    }

    public void setDisplayTitle(Title newTitle) {
        displayTitle = newTitle;
    }

    public Title getDisplayTitle() {
        return displayTitle;
    }

    public boolean getGenderSetting() {
        return genderSetting;
    }

    public void setGenderSetting(boolean newSetting) {
        genderSetting = newSetting;
    }

    public CompoundTag serializeNBT() {
        CompoundTag data = new CompoundTag();
        data.putString(NICKNAME, nickname);
        data.putBoolean(GENDER_SETTING, genderSetting);
        data.putString(DISPLAY_TITLE, getDisplayTitle().getID().toString());
        ListTag obtained = new ListTag();
        for (Title title : obtainedTitles) {
            obtained.add(StringTag.valueOf(title.getID().toString()));
        }
        data.put(OBTAINED_TITLES, obtained);

        CompoundTag timesTag = new CompoundTag();
        for (Map.Entry<String, TimeData> entry : titleObtainTimes.entrySet()) {
            timesTag.put(entry.getKey(), entry.getValue().serializeNBT());
        }
        data.put(TITLE_OBTAIN_TIMES, timesTag);

        return data;
    }

    public void deserializeNBT(CompoundTag nbt) {
        obtainedTitles.clear();
        titleObtainTimes.clear();
        nickname = nbt.getString(NICKNAME);
        genderSetting = nbt.getBoolean(GENDER_SETTING);
        displayTitle = TitleManager.getTitle(nbt.getString(DISPLAY_TITLE));
        ListTag obtained = (ListTag) nbt.get(OBTAINED_TITLES);
        for (int i = 0; i < obtained.size(); i++) {
            Title title = TitleManager.getTitle(obtained.getString(i));
            if (title == null) continue;
            obtainedTitles.add(title);
        }

        if (nbt.contains(TITLE_OBTAIN_TIMES)) {
            CompoundTag timesTag = nbt.getCompound(TITLE_OBTAIN_TIMES);
            for (String titleId : timesTag.getAllKeys()) {
                CompoundTag timeTag = timesTag.getCompound(titleId);
                TimeData timeData = TimeData.deserializeNBT(timeTag);
                if (timeData != null) {
                    titleObtainTimes.put(titleId, timeData);
                }
            }
        }
    }

    public static class Provider implements ICapabilitySerializable<CompoundTag> {
        protected final TitlesCapability capInstance;
        protected final LazyOptional<TitlesCapability> capOptional;

        public Provider() {
            this.capInstance = new TitlesCapability();
            this.capOptional = LazyOptional.of(() -> this.capInstance);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if (cap == TITLES_CAPABILITY) {
                return capOptional.cast();
            }
            return LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            return capInstance.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            capInstance.deserializeNBT(nbt);
        }
    }
}