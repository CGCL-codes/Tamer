        public void actionPerformed(ActionEvent event) {
            JFileChooser fileDialog = new JFileChooser(dataFile);
            ChanukahFileFilter filter = new ChanukahFileFilter("xml", "Chanukah Address Database");
            fileDialog.addChoosableFileFilter(filter);
            int status = fileDialog.showSaveDialog(mainView);
            if (status == JFileChooser.APPROVE_OPTION) {
                String file = fileDialog.getSelectedFile().getAbsolutePath();
                if ((fileDialog.getFileFilter().getClass() == ChanukahFileFilter.class) && (file.charAt(file.length() - 4) != '.')) {
                    file += ("." + ((ChanukahFileFilter) fileDialog.getFileFilter()).getExtension());
                }
                if (new File(file).exists()) {
                    GenericChanukahDialog dialog = new GenericChanukahDialog(mainView, "The selected file exists already, Do you want to overwrite it?", true, GenericChanukahDialog.BUTTONS.BUTTON_YES | GenericChanukahDialog.BUTTONS.BUTTON_NO, GenericChanukahDialog.TYPES.TYPE_QUESTION);
                    dialog.show();
                    if (dialog.getStatus() == GenericChanukahDialog.BUTTONS.BUTTON_NO) {
                        return;
                    }
                }
                dataFile = file;
                if (ChanukahStorage.saveData(rootGroup, dataFile)) {
                } else {
                    new GenericChanukahDialog(mainView, "Couldn't save " + dataFile, true, GenericChanukahDialog.BUTTONS.BUTTON_OK, GenericChanukahDialog.TYPES.TYPE_ERROR).show();
                }
            }
            setTitle();
        }
