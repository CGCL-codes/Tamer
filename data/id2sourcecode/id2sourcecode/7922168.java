    public void run(IAction action) {
        TreeViewer viewer = (TreeViewer) vview.getViewer();
        this.varList = new ObjectGraphBuilder().buildFromSelection((ITreeSelection) viewer.getSelection(), (JDIStackFrame) viewer.getInput());
        FileDialog dialog = new FileDialog(vview.getSite().getShell(), SWT.SAVE);
        dialog.setFilterExtensions(new String[] { "*.bvars" });
        String open = dialog.open();
        if (open != null && open.trim().length() != 0) {
            File file = new File(open);
            if (file.exists()) {
                String message = "The file you selected already exists, overwrite?";
                boolean overwrite = MessageDialogWithToggle.openQuestion(vview.getSite().getShell(), "Variable file already exists", message);
                if (overwrite) write(file, this.varList);
                return;
            }
            write(file, this.varList);
        }
    }
