    protected int execute(ActionData actionData) {
        if (TuxGuitar.instance().getChannelManager().isDisposed()) {
            TuxGuitar.instance().getChannelManager().show();
        } else {
            TuxGuitar.instance().getChannelManager().dispose();
        }
        return 0;
    }
