    public int saveFile(Spreadsheet sheet, boolean promptForName) {
        PipedInputStream pipeIn = null;
        PipedOutputStream pipeOut = null;
        try {
            FileSaveService fss = (FileSaveService) ServiceManager.lookup("javax.jnlp.FileSaveService");
            if (fss == null) {
                return Controller.CANCEL_OPTION;
            }
            pipeIn = new PipedInputStream();
            pipeOut = new PipedOutputStream(pipeIn);
            Thread writeThread = new Thread(new Writer(sheet, pipeOut));
            writeThread.setDaemon(true);
            writeThread.start();
            Controller controller = getController();
            String hint = getClue(_fc, controller.getSheetSource());
            if (promptForName && _fc != null) {
                _fc = fss.saveAsFileDialog(hint, new String[] { "hwks" }, _fc);
            } else {
                _fc = fss.saveFileDialog(hint, new String[] { "hwks" }, pipeIn, hint);
            }
            if (_fc == null) {
                return Controller.CANCEL_OPTION;
            }
            controller.setSheetSource(new URL("file:///" + _fc.getName()));
            controller.setSheetDirty(false);
            pipeIn.close();
            return Controller.YES_OPTION;
        } catch (Exception x) {
            displayError(x);
            return Controller.CANCEL_OPTION;
        }
    }
