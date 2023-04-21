    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setEditDomain(new DefaultEditDomain(this));
        getCommandStack().addCommandStackEventListener(new CommandStackEventListener() {

            public void stackChanged(CommandStackEvent event) {
                TextCommand command = (TextCommand) event.getCommand();
                if (command != null) {
                    GraphicalTextViewer textViewer = (GraphicalTextViewer) getGraphicalViewer();
                    if (event.getDetail() == CommandStack.POST_EXECUTE) textViewer.setSelectionRange(command.getExecuteSelectionRange(textViewer)); else if (event.getDetail() == CommandStack.POST_REDO) textViewer.setSelectionRange(command.getRedoSelectionRange(textViewer)); else if (event.getDetail() == CommandStack.POST_UNDO) textViewer.setSelectionRange(command.getUndoSelectionRange(textViewer));
                }
            }
        });
        super.init(site, input);
        site.getKeyBindingService().setScopes(new String[] { GEFActionConstants.CONTEXT_TEXT });
        site.getActionBars().setGlobalActionHandler(ActionFactory.UNDO.getId(), getActionRegistry().getAction(ActionFactory.UNDO.getId()));
        site.getActionBars().setGlobalActionHandler(ActionFactory.REDO.getId(), getActionRegistry().getAction(ActionFactory.REDO.getId()));
    }
