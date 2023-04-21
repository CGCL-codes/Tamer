    public static File saveFile(String title, String path, String filedescr, String extension, boolean checkexist) {
        File f = new File(lastpath, path);
        JFileChooser fc = new JFileChooser(path);
        fc.setDialogTitle(title);
        fc.setFileFilter(new FileNameExtensionFilter(filedescr, extension));
        fc.setSelectedFile(f);
        int option = fc.showSaveDialog(null);
        if (JFileChooser.APPROVE_OPTION == option) {
            File newfile = fc.getSelectedFile();
            boolean cansave = true;
            if (checkexist && newfile.exists()) {
                int saveresult = JOptionPane.showConfirmDialog(null, "File already exists, do you really want to overwrite?");
                if (saveresult != JOptionPane.YES_OPTION) {
                    cansave = false;
                }
            }
            if (cansave) {
                Utils.lastpath = newfile.getParent();
                return newfile;
            }
        }
        return null;
    }
