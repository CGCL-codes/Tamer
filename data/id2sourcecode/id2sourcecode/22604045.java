    @Override
    protected void doCopy(Clipboard clipboard, TextTransfer plainTextTransfer, StringBuffer buffer) {
        FileDialog d = new FileDialog(shell, SWT.SAVE);
        d.setFilterExtensions(new String[] { "*.txt", "*.vars" });
        String open = d.open();
        if (open != null && open.trim().length() != 0) {
            File file = new File(open);
            if (file.exists()) {
                String message = "The file you selected already exists, overwrite?";
                boolean overwrite = MessageDialogWithToggle.openQuestion(shell, "Variable file already exists", message);
                if (overwrite) write(file, buffer.toString());
                return;
            }
            write(file, buffer.toString());
        }
    }
