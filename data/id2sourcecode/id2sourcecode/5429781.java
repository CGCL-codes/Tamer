    private void handleNewView(View e) {
        if (numberOfViews < numberOfChannels) {
            numberOfViews++;
            return;
        }
        numberOfViews = 1;
        isBlocked = false;
        vs = e.vs;
        try {
            e.go();
        } catch (AppiaEventException e2) {
            e2.printStackTrace();
        }
        while (!eventsPending.isEmpty()) {
            final GroupSendableEvent event = eventsPending.remove();
            try {
                event.view_id = vs.id;
                event.setSourceSession(this);
                event.init();
                event.go();
            } catch (AppiaEventException e1) {
                e1.printStackTrace();
            }
        }
        mailbox.add(e);
        if (requestedLeave && leaveChannel != null) sendLeave(e.getChannel());
    }
