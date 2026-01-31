package aurilux.titles.compat.ftbq.rewards;

import aurilux.titles.common.TitlesMod;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftbquests.quest.reward.RewardType;
import dev.ftb.mods.ftbquests.quest.reward.RewardTypes;

public interface FTBRewardTypes {
    RewardType TITLE = RewardTypes.register(TitlesMod.prefix("title"), TitleReward::new, () -> Icons.CONTROLLER);

    static void init() {}
}
