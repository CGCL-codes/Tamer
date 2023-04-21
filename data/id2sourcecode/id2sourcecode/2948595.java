    public String showSaveWindow() {
        JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.dir")));
        chooser.showSaveDialog(chooser);
        String filepath = chooser.getSelectedFile().getAbsolutePath();
        if (!filepath.substring(filepath.length() - 4, filepath.length()).equalsIgnoreCase(".bmp")) {
            filepath += ".BMP";
        }
        if (new File(filepath).exists()) {
            int res = JOptionPane.showConfirmDialog(new JButton(), "File Already Exists! Are you sure you want to overwrite?", "RainSquared Owl", JOptionPane.OK_CANCEL_OPTION);
            System.out.println(res);
            if (res != 0) {
                return "";
            }
        }
        return filepath;
    }
