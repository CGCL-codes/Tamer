    public void doExecute() throws VFSException {
        JFD jfd = getJFD();
        JFDModel model = jfd.getModel();
        JFDDialog dialog = null;
        try {
            model.lockAutoUpdate(this);
            VFile selectedFile = model.getSelectedFile();
            VFile[] markedFiles = model.getMarkedFiles();
            if (selectedFile.isDirectory() && markedFiles.length == 0) {
                return;
            }
            FileSystem fileSystem = model.getCurrentDirectory().getFileSystem();
            if (fileSystem instanceof FileListFileSystem) {
                removeFileList((FileListFileSystem) fileSystem, selectedFile, markedFiles);
                return;
            }
            dialog = jfd.createDialog();
            dialog.setTitle(JFDResource.LABELS.getString("title_delete"));
            if (markedFiles.length == 0) {
                Object[] params = { selectedFile.getName() };
                dialog.addMessage(MessageFormat.format(JFDResource.MESSAGES.getString("message_delete_file"), params));
            } else {
                dialog.addMessage(JFDResource.MESSAGES.getString("message_delete_marked_files"));
            }
            boolean defaultOk = ((Boolean) jfd.getCommonConfiguration().getParam("delete_ok_default", Boolean.TRUE)).booleanValue();
            dialog.addButton(OK, JFDResource.LABELS.getString("ok"), 'y', defaultOk);
            dialog.addButton(CANCEL, JFDResource.LABELS.getString("cancel"), 'n', !defaultOk);
            dialog.pack();
            dialog.setVisible(true);
            String answer = dialog.getButtonAnswer();
            if (answer == null || CANCEL.equals(answer)) {
                return;
            }
            showProgress(1000);
            Manipulation[] children;
            DeleteFailurePolicy policy = new JFDDeleteFailurePolicy(getJFD());
            if (markedFiles.length > 0) {
                children = new Manipulation[markedFiles.length];
                for (int i = 0; i < markedFiles.length; i++) {
                    children[i] = FileUtil.prepareDelete(markedFiles[i], policy, this);
                    children[i].setParentManipulation(this);
                    children[i].prepare();
                }
            } else {
                children = new Manipulation[1];
                children[0] = FileUtil.prepareDelete(selectedFile, policy, this);
                children[0].setParentManipulation(this);
                children[0].prepare();
            }
            setChildManipulations(children);
            for (int i = 0; i < children.length; i++) {
                children[i].execute();
            }
        } finally {
            model.unlockAutoUpdate(this);
            model.refresh();
            if (dialog != null) {
                dialog.dispose();
            }
        }
    }
