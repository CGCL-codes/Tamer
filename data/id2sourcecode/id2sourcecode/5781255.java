    CBListener getCommentBarListener() {
        return new CBListener() {

            public void copy(CBEvent ev) {
                log.debug("copy started");
                clipboard = TextActions.getClipboard(ev.getTa());
            }

            public void cut(CBEvent ev) {
                log.debug("cut started");
                clipboard = TextActions.getClipboard(ev.getTa());
                ((MyUndoableEditListener) ((DefaultStyledDocument) ev.getSrc().getDocument()).getUndoableEditListeners()[0]).sendCloseEvent();
            }

            public void paste(CBEvent ev) {
                log.debug("paste started");
                MyUndoableEditListener muel = (MyUndoableEditListener) ((DefaultStyledDocument) ev.getSrc().getDocument()).getUndoableEditListeners()[0];
                if (clipboard != null) {
                    muel.sendOpenEvent();
                    TextAction ta = ev.getTa();
                    TextActions.setClipboard(ta, clipboard);
                    ta.actionPerformed(new ActionEvent(ev.getSrc(), 0, ""));
                    muel.sendCloseEvent();
                }
            }

            public void doRedo() {
                if (redoable.size() > 0) {
                    redoable.peek().redo();
                    undoable.offer(redoable.poll());
                }
            }

            public void doUndo() {
                if (undoable.size() > 0) {
                    undoable.peek().undo();
                    redoable.offer(undoable.poll());
                }
            }

            public void sendCutEvent(CBEvent ev) {
                ((MyUndoableEditListener) ((DefaultStyledDocument) ev.getSrc().getDocument()).getUndoableEditListeners()[0]).sendOpenEvent();
            }
        };
    }
