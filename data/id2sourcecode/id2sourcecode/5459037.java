    public static String selectPath(int di) {
        JFrame frame = new JFrame();
        String fileName = File.separator + "xml";
        JFileChooser fc = new JFileChooser(new File(fileName));
        int f = 0;
        File file = null;
        do {
            switch(di) {
                case (0):
                    fc.showSaveDialog(frame);
                    file = fc.getSelectedFile();
                    try {
                        if (file.exists()) {
                            f = confirmDialog("file`s already exist", "do you want to overwrite it?");
                        }
                    } catch (java.lang.NullPointerException e) {
                        f = 0;
                    }
                    break;
                case (1):
                    fc = new JFileChooser(new File(Util.getIngredientsDirectory()));
                    fc.showOpenDialog(frame);
                    file = fc.getSelectedFile();
                    break;
                case (2):
                    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    fc.showOpenDialog(frame);
                    file = fc.getSelectedFile();
                    break;
                case (3):
                    fc = new JFileChooser(new File(Util.getHowtosDirectory()));
                    fc.showOpenDialog(frame);
                    file = fc.getSelectedFile();
                    break;
            }
        } while (f != 0);
        String path = "";
        try {
            if (di != 0) {
                path += file.getAbsoluteFile();
            } else {
                path = file.getAbsolutePath() + ".xml";
            }
        } catch (java.lang.NullPointerException ex) {
        }
        return path;
    }
