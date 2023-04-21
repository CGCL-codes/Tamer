package diet.server.CbyC;

import java.util.Vector;
import diet.client.WYSIWYGDocumentWithEnforcedTurntaking;
import diet.debug.Debug;
import diet.message.MessageCBYCChangeTurntakingStatus;
import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.ConversationController.CCCBYCDefaultController;

public class FloorHolderCRProcessing extends FloorHolderAdvanced {

    public Participant fragAppOrig = null;

    public FloorHolderCRProcessing(CCCBYCDefaultController cC) {
        super(cC);
    }

    public synchronized void openFloorAfterTimeOut(long timeout) {
        if (fragAppOrig != null) {
            openFloorAfterTimeOutForParticipants(timeout, this.cC.getC().getParticipants().getAllOtherParticipants(fragAppOrig));
        } else super.openFloorAfterTimeOut(timeout);
    }

    private synchronized void openFloorAfterTimeOutForParticipants(long timeout, Vector participants) {
        if (floorHolder != null) {
            if (floorHolder.isTyping(timeout)) {
                return;
            }
            if (!sS.canFloorBeRelinquished()) return;
            for (int i = 0; i < participants.size(); i++) {
                Participant p = (Participant) participants.elementAt(i);
                MessageCBYCChangeTurntakingStatus mcbycOther = new MessageCBYCChangeTurntakingStatus("server", "server", WYSIWYGDocumentWithEnforcedTurntaking.nooneelsetyping, null);
                p.sendMessage(mcbycOther);
                if (Debug.showCounterAfterCBYCStatusMessage) {
                    cC.getC().sendLabelDisplayAndEnableToParticipantInOwnStatusWindow(p, "Please type " + cCounter, false, true);
                } else {
                    cC.getC().sendLabelDisplayAndEnableToParticipantInOwnStatusWindow(p, "Please type", false, true);
                }
            }
            cCounter++;
            floorHolder = null;
        }
    }

    public void openFloorAfterFragSending() {
        this.sendingFrag(false);
        if (fragAppOrig != null) {
            changeFloorStatusOfParticipantsNoPrefix(cC.getC().getParticipants().getAllOtherParticipants(fragAppOrig), WYSIWYGDocumentWithEnforcedTurntaking.nooneelsetyping);
            for (Object o : this.cC.getC().getParticipants().getAllOtherParticipants(fragAppOrig)) {
                Participant p = (Participant) o;
                if (Debug.showCounterAfterCBYCStatusMessage) {
                    cC.getC().sendLabelDisplayAndEnableToParticipantInOwnStatusWindow(p, "Please type " + cCounter, false, true);
                } else {
                    cC.getC().sendLabelDisplayAndEnableToParticipantInOwnStatusWindow(p, "Please type", false, true);
                }
            }
            floorHolder = null;
        }
    }

    protected synchronized void giveFloorToParticipantPrefix(Participant sender) {
        Conversation.printWSln("Floor Holder", "giveFloor In CR Processing");
        Vector v2 = cC.getC().getParticipants().getAllOtherParticipants(sender);
        Vector v = (Vector) v2.clone();
        if (fragAppOrig != null) {
            Conversation.printWSln("FloorHolder", "Removing appOrig");
            if (v.remove(fragAppOrig)) Conversation.printWSln("FloorHolder", "Successful"); else Conversation.printWSln("FloorHolder", "Unsuccessful");
        }
        for (int i = 0; i < v.size(); i++) {
            Participant p = (Participant) v.elementAt(i);
            diet.message.MessageCBYCChangeTurntakingStatus mcbyc = new MessageCBYCChangeTurntakingStatus("server", "server", WYSIWYGDocumentWithEnforcedTurntaking.othertyping, DocInsert.createEmpty());
            p.sendMessage(mcbyc);
        }
        DocInsert prefix;
        if (diet.debug.Debug.showPrefixBeforeCBYCTurn) {
            prefix = sm.getPrefixForParticipant("" + oCounter + "fh", sender, sender);
        } else {
            prefix = sm.getPrefixForParticipant("", sender, sender);
        }
        MessageCBYCChangeTurntakingStatus mcbyc = new MessageCBYCChangeTurntakingStatus("server", "server", WYSIWYGDocumentWithEnforcedTurntaking.typingunhindered, prefix);
        sender.sendMessage(mcbyc);
        if (Debug.showCounterAfterCBYCStatusMessage) {
            cC.getC().sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(sender, "You are typing " + oCounter, true, true);
        } else {
            cC.getC().sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(sender, "You are typing", true, true);
        }
        forceInformOthersOfTyping(sender);
        oCounter++;
        floorHolder = sender;
    }

    protected synchronized void giveFloorToParticipantNoPrefix(Participant sender) {
        Conversation.printWSln("Floor Holder", "giveFloor In CR Processing");
        Vector v2 = cC.getC().getParticipants().getAllOtherParticipants(sender);
        Vector v = (Vector) v2.clone();
        if (fragAppOrig != null) {
            Conversation.printWSln("FloorHolder", "Removing appOrig");
            if (v.remove(fragAppOrig)) Conversation.printWSln("FloorHolder", "Successful"); else Conversation.printWSln("FloorHolder", "Unsuccessful");
        }
        for (int i = 0; i < v.size(); i++) {
            Participant p = (Participant) v.elementAt(i);
            diet.message.MessageCBYCChangeTurntakingStatus mcbyc = new MessageCBYCChangeTurntakingStatus("server", "server", WYSIWYGDocumentWithEnforcedTurntaking.othertyping, DocInsert.createEmpty());
            p.sendMessage(mcbyc);
        }
        MessageCBYCChangeTurntakingStatus mcbyc = new MessageCBYCChangeTurntakingStatus("server", "server", WYSIWYGDocumentWithEnforcedTurntaking.typingunhindered, DocInsert.createEmpty());
        sender.sendMessage(mcbyc);
        if (Debug.showCounterAfterCBYCStatusMessage) {
            cC.getC().sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(sender, "You are typing" + oCounter, true, true);
        } else {
            cC.getC().sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(sender, "You are typing", true, true);
        }
        this.forceInformOthersOfTyping(sender);
        oCounter++;
        floorHolder = sender;
    }

    boolean sendingFrag = false;

    public void processFloorRequest(Participant sender, MessageCBYCTypingUnhinderedRequest mtur) {
        if (this.fragAppOrig == null) super.processFloorRequest(sender, mtur); else {
            if (this.sendingFrag) {
                super.changeFloorStatusOfParticipantNoPrefix(sender, diet.client.CBYCDocumentWithEnforcedTurntaking.othertyping);
                return;
            } else {
                super.processFloorRequest(sender, mtur);
            }
        }
    }

    public synchronized void sendingFrag(boolean b) {
        this.sendingFrag = b;
    }

    public boolean nobodyTypingAndFloorIsOpen(long timeout) {
        if (floorHolder != null) return false;
        Vector v = cC.getC().getParticipants().getAllParticipants();
        for (Object o : v) {
            Participant p = (Participant) o;
            if (p.isTyping(timeout)) {
                return false;
            }
        }
        return true;
    }

    public boolean floorIsOpen() {
        return floorHolder == null;
    }

    public synchronized void openFloorResetAllToNormal() {
        this.sendingFrag(false);
        if (fragAppOrig != null) this.fragAppOrig = null;
        changeFloorStatusOfParticipantsNoPrefix(cC.getC().getParticipants().getAllParticipants(), WYSIWYGDocumentWithEnforcedTurntaking.nooneelsetyping);
        for (Object o : this.cC.getC().getParticipants().getAllParticipants()) {
            Participant p = (Participant) o;
            if (Debug.showCounterAfterCBYCStatusMessage) {
                cC.getC().sendLabelDisplayAndEnableToParticipantInOwnStatusWindow(p, "Please type " + cCounter, false, true);
            } else {
                cC.getC().sendLabelDisplayAndEnableToParticipantInOwnStatusWindow(p, "Please type", false, true);
            }
        }
    }

    public void forceInformOthersOfTyping(Participant actualTypist) {
        infCounter++;
        Vector v2 = cC.getC().getParticipants().getAllOtherParticipants(actualTypist);
        Vector v = (Vector) v2.clone();
        if (this.fragAppOrig != null) {
            Conversation.printWSln("FloorHolder", "Removing appOrig in inform others");
            v.remove(fragAppOrig);
        }
        if (v.size() == 0) Conversation.printWSln("FloorHolder", "sending inform to nobody");
        for (int i = 0; i < v.size(); i++) {
            Participant p = (Participant) v.elementAt(i);
            Conversation.printWSln("FloorHolder", "sending inform to:" + p.getUsername());
            String statusWindowDisplay = sS.getStatusWindowTextForRecipient(p.getUsername());
            if (Debug.showCounterAfterCBYCStatusMessage) {
                cC.getC().sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(p, statusWindowDisplay + infCounter, true, true);
            } else {
                cC.getC().sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(p, statusWindowDisplay, true, true);
            }
        }
    }
}
