    public boolean validateEditorInputState() {
        boolean enabled = false;
        synchronized (this) {
            enabled = fIsStateValidationEnabled;
        }
        if (enabled) {
            GraphicalViewer viewer = getGraphicalViewer();
            if (viewer == null) return false;
            final IEditorInput input = getEditorInput();
            BusyIndicator.showWhile(getSite().getShell().getDisplay(), new Runnable() {

                public void run() {
                    validateState(input);
                }
            });
            sanityCheckState(input);
            return !isEditorInputReadOnly();
        }
        return !isEditorInputReadOnly();
    }
