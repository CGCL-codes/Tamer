    public static void executeCommand(final Command cmd, IGraphicalEditPart part) {
        Map<String, Boolean> options = null;
        boolean isActivating = true;
        EditPartViewer viewer = part.getViewer();
        if (viewer instanceof DiagramGraphicalViewer) {
            isActivating = ((DiagramGraphicalViewer) viewer).isInitializing();
        }
        if (isActivating || !EditPartUtil.isWriteTransactionInProgress(part, false, false)) options = Collections.singletonMap(Transaction.OPTION_UNPROTECTED, Boolean.TRUE);
        AbstractEMFOperation operation = new AbstractEMFOperation((part).getEditingDomain(), StringStatics.BLANK, options) {

            @Override
            protected IStatus doExecute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
                cmd.execute();
                return Status.OK_STATUS;
            }
        };
        try {
            operation.execute(new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            M3ActionsDiagramEditorPlugin.getInstance().logError(Messages.EReferenceUtils_CanNotExecute, e);
        }
    }
