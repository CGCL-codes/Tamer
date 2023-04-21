    public int saveFile(Spreadsheet sheet, boolean promptForName) {
        URL hint = _controller.getSheetSource();
        if (promptForName || hint == null) if (getChooser().showSaveDialog(_frame) != JFileChooser.APPROVE_OPTION) return Controller.CANCEL_OPTION;
        File file = getChosenFile();
        if (file != null) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                _controller.setSheetSource(file.toURI().toURL());
                _sheet.writeSpreadsheet(fos);
                fos.close();
                _controller.setSheetDirty(false);
                return Controller.YES_OPTION;
            } catch (IOException x) {
                Throwable t = Controller.getRootCause(x);
                _controller.notify(x.getMessage(), Controller.getExceptionName(t));
            }
        }
        return Controller.CANCEL_OPTION;
    }
