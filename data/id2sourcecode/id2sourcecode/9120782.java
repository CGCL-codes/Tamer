    public void addFiles(File directory, boolean addDirectories) {
        if (directory.isDirectory()) {
            boolean addThem = true, addFolders = addDirectories;
            final File files[] = directory.listFiles();
            if (!addFolders) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        addFolders = (JOptionPane.showConfirmDialog(this, "This Folder Contains Other Folders. Do You Wish To Add All Files From These Folders?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
                        break;
                    }
                }
            }
            for (int i = 0; i < files.length; i++) {
                String fname = files[i].getName();
                if (fname.toLowerCase().endsWith(".exp")) {
                    File expTry = new File(openProject.getPath() + fname.substring(0, fname.lastIndexOf(File.separator) + 1) + fname);
                    if (expTry.exists()) {
                        JOptionPane.showMessageDialog(this, "One Or More Expression Files Already Exist In The Project.\n" + "You May Not Overwrite These Files Since These Files Are Required By Other Files\n" + "Review The Users Manual For More Information\n" + "You May Still Add Individual Files From This Folder Through Project->Add File");
                        addThem = false;
                        break;
                    }
                }
            }
            if (addThem) {
                Thread r = new Thread() {

                    public void run() {
                        ProgressFrame progressbar = new ProgressFrame("Adding Files...");
                        jdesktoppane.add(progressbar);
                        progressbar.setMaximum(files.length);
                        progressbar.setVisible(true);
                        for (int i = 0; i < files.length; i++) {
                            if (files[i].isFile()) {
                                progressbar.setTitle("Adding " + files[i].getName());
                                addFile(files[i]);
                                progressbar.addValue(1);
                            } else {
                                addFiles(files[i], true);
                            }
                        }
                        progressbar.dispose();
                    }
                };
                r.start();
            }
        }
    }
