    @Override
    public void run() {
        Utils.runSafeSync(log, new Runnable() {

            public void run() {
                List<User> participants = SelectionRetrieverFactory.getSelectionRetriever(User.class).getSelection();
                if (participants.size() == 1) {
                    if (!participants.get(0).hasWriteAccess()) {
                        sarosUI.performPermissionChange(participants.get(0), Permission.WRITE_ACCESS);
                        updateEnablement();
                    } else {
                        log.warn("Participant has already write access: " + participants.get(0));
                    }
                } else {
                    log.warn("More than one participant selected.");
                }
            }
        });
    }
