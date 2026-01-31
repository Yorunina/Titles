package aurilux.titles.common.core;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.Date;

/**
 * Represents the time data when a title was obtained.
 * Stores both game time (ticks) and real system time (milliseconds since epoch).
 */
public class TimeData {
    private final long gameTime;
    private final long realTime;

    public TimeData(Date date, long gameTime) {
        this.realTime = date.getTime();
        this.gameTime = gameTime;
    }

    public TimeData(long gameTime, long realTime) {
        this.gameTime = gameTime;
        this.realTime = realTime;
    }

    /**
     * Creates a TimeData instance with the current game time and real time.
     * @param levelTime The current game time (ticks)
     * @return A new TimeData instance
     */
    public static TimeData now(long levelTime) {
        return new TimeData(levelTime, System.currentTimeMillis());
    }

    public long getGameTime() {
        return gameTime;
    }

    public long getRealTime() {
        return realTime;
    }

    public Date getDate() {
        return new Date(realTime);
    }

    /**
     * Serializes this TimeData to NBT.
     * @return The CompoundTag containing the time data
     */
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putLong("gameTime", gameTime);
        tag.putLong("realTime", realTime);
        return tag;
    }

    /**
     * Deserializes a TimeData from NBT.
     * @param tag The CompoundTag containing the time data
     * @return A new TimeData instance, or null if the tag is invalid
     */
    public static TimeData deserializeNBT(CompoundTag tag) {
        if (tag.contains("gameTime", Tag.TAG_LONG) && tag.contains("realTime", Tag.TAG_LONG)) {
            return new TimeData(tag.getLong("gameTime"), tag.getLong("realTime"));
        }
        return null;
    }

    @Override
    public String toString() {
        return "TimeData{gameTime=" + gameTime + ", realTime=" + realTime + "}";
    }
}