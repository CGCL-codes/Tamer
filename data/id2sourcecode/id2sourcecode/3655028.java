    public int shouldOverWrite(AbstractFile source, AbstractFile alreadyExistingFile) {
        Object[] options = { Message.getInstance().getMessage(Batavia.getLocale(), "button.overwrite", "Overwrite"), Message.getInstance().getMessage(Batavia.getLocale(), "button.skip", "Skip"), Message.getInstance().getMessage(Batavia.getLocale(), "button.all", "All"), Message.getInstance().getMessage(Batavia.getLocale(), "button.none", "None"), Message.getInstance().getMessage(Batavia.getLocale(), "button.if_source_newer", "If source file is newer"), Message.getInstance().getMessage(Batavia.getLocale(), "button.abort", "Abort") };
        int code[] = { TransferInputHandler.YES, TransferInputHandler.NO, TransferInputHandler.ALL, TransferInputHandler.NONE, TransferInputHandler.NEWER, TransferInputHandler.ABORT };
        int answer = JOptionPane.showOptionDialog(Batavia.getCurrentCommanderWindow(), Message.getInstance().getMessage(Batavia.getLocale(), "dialog.overwrite_this_file_with", "Overwrite this file {0} with {1}", new Object[] { alreadyExistingFile.getPath(), source.getPath() }), Message.getInstance().getMessage(Batavia.getLocale(), "title.warning", "Warning"), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (answer == JOptionPane.CLOSED_OPTION) {
            return NO;
        } else {
            return code[answer];
        }
    }
