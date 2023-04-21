    @Override
    public String queryOverwrite(String pathString) {
        Path path = new Path(pathString);
        String messageString;
        if (path.getFileExtension() == null || path.segmentCount() < 2) {
            messageString = NLS.bind("''{0}'' already exists.  Would you like to overwrite it?", pathString);
        } else {
            messageString = NLS.bind("Overwrite ''{0}'' in folder ''{1}''?", path.lastSegment(), path.removeLastSegments(1).toOSString());
        }
        final MessageDialog dialog = new MessageDialog(getContainer().getShell(), "Question", null, messageString, MessageDialog.QUESTION, new String[] { IDialogConstants.YES_LABEL, IDialogConstants.YES_TO_ALL_LABEL, IDialogConstants.NO_LABEL, IDialogConstants.NO_TO_ALL_LABEL, IDialogConstants.CANCEL_LABEL }, 0) {

            @Override
            protected int getShellStyle() {
                return super.getShellStyle() | SWT.SHEET;
            }
        };
        String[] response = new String[] { YES, ALL, NO, NO_ALL, CANCEL };
        getControl().getDisplay().syncExec(new Runnable() {

            @Override
            public void run() {
                dialog.open();
            }
        });
        return dialog.getReturnCode() < 0 ? CANCEL : response[dialog.getReturnCode()];
    }
