    public void refreshState() {
        for (Object m : md.getChannel().getView().getMembers()) {
            new Thread(new StateRequester((Address) m)).start();
        }
    }
