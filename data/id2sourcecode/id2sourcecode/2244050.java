    public ListSelectionListener createListSelectionListener(VFSJFileChooser fc) {
        return new SelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    VFSJFileChooser chooser = getFileChooser();
                    VFSFileSystemView fsv = chooser.getFileSystemView();
                    JList list = (JList) e.getSource();
                    if (chooser.isMultiSelectionEnabled()) {
                        FileObject[] files = null;
                        Object[] objects = list.getSelectedValues();
                        if (objects != null) {
                            if ((objects.length == 1) && (VFSUtils.isDirectory((FileObject) objects[0])) && chooser.isTraversable(((FileObject) objects[0])) && ((chooser.getFileSelectionMode() == chooser.FILES_ONLY) || !fsv.isFileSystem(((FileObject) objects[0])))) {
                                setDirectorySelected(true);
                                setDirectory(((FileObject) objects[0]));
                            } else {
                                files = new FileObject[objects.length];
                                int j = 0;
                                for (int i = 0; i < objects.length; i++) {
                                    FileObject f = (FileObject) objects[i];
                                    boolean isDir = VFSUtils.isDirectory(f);
                                    boolean isFile = !isDir;
                                    if ((chooser.isFileSelectionEnabled() && isFile) || (chooser.isDirectorySelectionEnabled() && fsv.isFileSystem(f) && isDir)) {
                                        files[j++] = f;
                                    }
                                }
                                if (j == 0) {
                                    files = null;
                                } else if (j < objects.length) {
                                    FileObject[] tmpFiles = new FileObject[j];
                                    System.arraycopy(files, 0, tmpFiles, 0, j);
                                    files = tmpFiles;
                                }
                                setDirectorySelected(false);
                            }
                        }
                        chooser.setSelectedFiles(files);
                    } else {
                        FileObject file = (FileObject) list.getSelectedValue();
                        if ((file != null) && VFSUtils.isDirectory(file) && chooser.isTraversable(file) && ((chooser.getFileSelectionMode() == chooser.FILES_ONLY) || !fsv.isFileSystem(file))) {
                            setDirectorySelected(true);
                            setDirectory(file);
                            chooser.setSelectedFile(null);
                        } else {
                            setDirectorySelected(false);
                            if (file != null) {
                                chooser.setSelectedFile(file);
                            }
                        }
                    }
                }
            }
        };
    }
