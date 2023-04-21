    private void handleGroupSendableEvent(GroupSendableEvent e) {
        GroupSendableEvent cloned = null;
        try {
            cloned = (GroupSendableEvent) e.cloneEvent();
        } catch (CloneNotSupportedException ex) {
            System.err.println("Error sending event");
        }
        cloned.setDir(Direction.invert(cloned.getDir()));
        cloned.setSourceSession(this);
        cloned.dest = e.source;
        cloned.source = myEndpt;
        cloned.orig = myRank;
        cloned.setChannel(e.getChannel());
        try {
            cloned.init();
            cloned.go();
        } catch (AppiaEventException ex) {
            System.err.println("Error Sending event");
        }
    }
