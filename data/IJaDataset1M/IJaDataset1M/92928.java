package mrusanov.fantasyruler.game.ui.components.messagebox.panels.factory;

import mrusanov.fantasyruler.localization.Localization;
import mrusanov.fantasyruler.game.mvc.gamefield.controller.GameFieldController;
import mrusanov.fantasyruler.game.ui.components.messagebox.panels.MessagePanel;
import mrusanov.fantasyruler.game.ui.components.messagebox.panels.YesNoDecisionPanel;
import mrusanov.fantasyruler.player.Player;
import mrusanov.fantasyruler.player.message.Message;
import mrusanov.fantasyruler.player.message.war.ProposeAllyJoinWarMessage;

public class ProposeAllyJoinWarMessagePanelFactory implements MessagePanelFactory {

    @Override
    public MessagePanel buildMessagePanel(GameFieldController gameFieldController, Message message) {
        return new YesNoDecisionPanel(gameFieldController, message, ProposeAllyJoinWarMessage.ACCEPT_JOIN_WAR, ProposeAllyJoinWarMessage.REFUSE_JOIN_WAR, "message.proposeJoinWar") {

            @Override
            protected String buildText(Message message, String messageKey) {
                ProposeAllyJoinWarMessage proposeAllyJoinWarMessage = (ProposeAllyJoinWarMessage) message;
                Player ally = proposeAllyJoinWarMessage.getSender();
                Player enemy = proposeAllyJoinWarMessage.getEnemy();
                return Localization.getString(messageKey, ally, enemy);
            }
        };
    }
}
