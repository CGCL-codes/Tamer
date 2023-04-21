    private void saveGraph() {
        JFileChooser chooser = new JFileChooser();
        FileFilter filter = new FileFilter() {

            public boolean accept(File f) {
                return f.isDirectory() || f.getAbsolutePath().toLowerCase().endsWith(".png");
            }

            public String getDescription() {
                return "PNG images";
            }
        };
        chooser.setFileFilter(filter);
        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = chooser.getSelectedFile();
                String path = selectedFile.getAbsolutePath();
                if (!path.toLowerCase().endsWith(".png")) {
                    path += ".png";
                    selectedFile = new File(path);
                }
                if (selectedFile.exists()) {
                    String message = "File [" + selectedFile.getName() + "] already exists. Do you want to overwrite it?";
                    int answer = JOptionPane.showConfirmDialog(this, message, "File exists", JOptionPane.YES_NO_OPTION);
                    if (answer == JOptionPane.NO_OPTION) {
                        return;
                    }
                }
                String absolutePath = selectedFile.getAbsolutePath();
                byte[] data = rrdGraph.getRrdGraphInfo().getBytes();
                RandomAccessFile f = new RandomAccessFile(absolutePath, "rw");
                try {
                    f.write(data);
                } finally {
                    f.close();
                }
            } catch (IOException e) {
                Util.error(this, "Could not save graph to file:\n" + e);
            }
        }
    }
