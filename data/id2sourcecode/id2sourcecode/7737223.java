    public boolean CopyFile(String directory, String filename) {
        if (directory == null || filename == null) return false;
        if (directory == "" || filename == "") return false;
        File folder = new File(disp.sketchPath(core.Folder_sub));
        if (!folder.exists()) folder.mkdir();
        File in = new File(directory, filename);
        File out = new File(disp.sketchPath(core.Folder_sub), filename);
        FileInputStream is;
        try {
            is = new FileInputStream(in);
            FileOutputStream os = new FileOutputStream(out);
            os.getChannel().transferFrom(is.getChannel(), 0, in.length());
            is.close();
            os.close();
        } catch (Exception e1) {
            return false;
        }
        return true;
    }
