package quest.sanctum;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.questEngine.handlers.QuestHandler;
import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import gameserver.questEngine.model.QuestStatus;

public class _3928TheClericPreceptorsTask extends QuestHandler {

    private static final int questId = 3928;

    private final int skillId = 1165;

    private final int mainNpcId = 203707;

    public _3928TheClericPreceptorsTask() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(mainNpcId).addOnQuestStart(questId);
        qe.setNpcQuestData(mainNpcId).addOnTalkEvent(questId);
        qe.setQuestSkillIds(skillId).add(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (env.getVisibleObject() instanceof Npc) targetId = ((Npc) env.getVisibleObject()).getNpcId();
        if (targetId == mainNpcId) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 26) return sendQuestDialog(env, 4762); else return defaultQuestStartDialog(env);
            } else if (qs.getStatus() == QuestStatus.REWARD) {
                return defaultQuestEndDialog(env);
            }
        }
        return false;
    }

    @Override
    public boolean onSkillUseEvent(QuestCookie env, int skillUsedId) {
        final Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) return false;
        int var = qs.getQuestVarById(0);
        if (qs.getStatus() != QuestStatus.START) return false;
        if (skillUsedId == skillId) {
            if (var >= 0 && var < 9) {
                qs.setQuestVarById(0, var + 1);
                updateQuestStatus(env);
                return true;
            } else if (var == 9) {
                qs.setQuestVarById(0, var + 1);
                updateQuestStatus(env);
                qs.setStatus(QuestStatus.REWARD);
                updateQuestStatus(env);
                return true;
            }
        }
        return false;
    }
}
