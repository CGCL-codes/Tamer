    public void updateChannel(int id, short bnk, short prg, short vol, short bal, short cho, short rev, short pha, short tre, String name) {
        TGChannel channel = getManager().getChannel(id);
        if (channel != null) {
            boolean programChange = (bnk != channel.getBank() || prg != channel.getProgram());
            UndoableModifyChannel undoable = UndoableModifyChannel.startUndo(id);
            getManager().updateChannel(id, bnk, prg, vol, bal, cho, rev, pha, tre, name);
            TuxGuitar.instance().getUndoableManager().addEdit(undoable.endUndo());
            TuxGuitar.instance().getFileHistory().setUnsavedFile();
            TuxGuitar.instance().updateCache(true);
            if (TuxGuitar.instance().getPlayer().isRunning()) {
                if (programChange) {
                    TuxGuitar.instance().getPlayer().updatePrograms();
                } else {
                    TuxGuitar.instance().getPlayer().updateControllers();
                }
            }
        }
    }
