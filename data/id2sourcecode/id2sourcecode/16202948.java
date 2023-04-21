    public static void fileSave(String startDirectory, InputStream istr, String defaultFilename) throws Exception {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(startDirectory));
        chooser.setDialogTitle(Resource.getPlainResourceString("Save"));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal != JFileChooser.APPROVE_OPTION) return;
        String s = chooser.getSelectedFile().getAbsolutePath();
        FileOutputStream ostr = new FileOutputStream(s);
        int b;
        while ((b = istr.read()) != -1) ostr.write(b);
        istr.close();
        ostr.close();
    }
