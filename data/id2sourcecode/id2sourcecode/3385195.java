            public void stackChanged(CommandStackEvent event) {
                TextCommand command = (TextCommand) event.getCommand();
                if (command != null) {
                    GraphicalTextViewer textViewer = (GraphicalTextViewer) getGraphicalViewer();
                    if (event.getDetail() == CommandStack.POST_EXECUTE) textViewer.setSelectionRange(command.getExecuteSelectionRange(textViewer)); else if (event.getDetail() == CommandStack.POST_REDO) textViewer.setSelectionRange(command.getRedoSelectionRange(textViewer)); else if (event.getDetail() == CommandStack.POST_UNDO) textViewer.setSelectionRange(command.getUndoSelectionRange(textViewer));
                }
            }
