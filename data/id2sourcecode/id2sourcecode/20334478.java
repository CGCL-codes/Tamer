    protected void handleImportAction() {
        File parent = (File) ((IStructuredSelection) getSelection()).getFirstElement();
        if (parent == null) {
            parent = _rootFolder;
        } else if (!parent.isDirectory()) {
            parent = parent.getParentFile();
        }
        if (parent.exists() && parent.isDirectory()) {
            FileDialog dialog = new FileDialog(getControl().getShell(), SWT.MULTI | SWT.OPEN);
            dialog.setText("Choose files to import");
            String str = dialog.open();
            if (str != null) {
                String folder = dialog.getFilterPath();
                String[] filenames = dialog.getFileNames();
                String[] files = new String[filenames.length];
                for (int i = 0; i < filenames.length; i++) {
                    File tmp = new File(folder, filenames[i]);
                    files[i] = tmp.getAbsolutePath();
                }
                try {
                    StrakerFileUtils.copyFilesWithProgressMonitor(parent, files, getControl().getShell());
                } catch (IOException ex) {
                    MessageDialog.openError(getControl().getShell(), "Import", ex.getMessage());
                }
                expandToLevel(parent, 1);
                refresh();
            }
        }
    }
