    public void showImportRobotDialog() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileFilter() {

            public boolean accept(File pathname) {
                if (pathname.isHidden()) {
                    return false;
                }
                if (pathname.isDirectory()) {
                    return true;
                }
                String filename = pathname.getName();
                if (filename.equals("robocode.jar")) {
                    return false;
                }
                int idx = filename.lastIndexOf('.');
                String extension = "";
                if (idx >= 0) {
                    extension = filename.substring(idx);
                }
                if (extension.equalsIgnoreCase(".jar")) {
                    return true;
                }
                if (extension.equalsIgnoreCase(".zip")) {
                    return true;
                }
                return false;
            }

            public String getDescription() {
                return "Jar Files";
            }
        });
        chooser.setDialogTitle("Select the robot .jar file to copy to " + manager.getRobotRepositoryManager().getRobotsDirectory());
        if (chooser.showDialog(getRobocodeFrame(), "Import") == JFileChooser.APPROVE_OPTION) {
            File inputFile = chooser.getSelectedFile();
            String fileName = chooser.getSelectedFile().getName();
            int idx = fileName.lastIndexOf('.');
            String extension = "";
            if (idx >= 0) {
                extension = fileName.substring(idx);
            }
            if (!extension.equalsIgnoreCase(".jar")) {
                fileName += ".jar";
            }
            File outputFile = new File(manager.getRobotRepositoryManager().getRobotsDirectory(), fileName);
            if (inputFile.equals(outputFile)) {
                JOptionPane.showMessageDialog(getRobocodeFrame(), outputFile.getName() + " is already in the robots directory!");
                return;
            }
            if (outputFile.exists()) {
                if (JOptionPane.showConfirmDialog(getRobocodeFrame(), outputFile + " already exists.  Overwrite?", "Warning", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            if (JOptionPane.showConfirmDialog(getRobocodeFrame(), "Robocode will now copy " + inputFile.getName() + " to " + outputFile.getParent(), "Import robot", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    Utils.copy(inputFile, outputFile);
                    manager.getRobotRepositoryManager().clearRobotList();
                    JOptionPane.showMessageDialog(getRobocodeFrame(), "Robot imported successfully.");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(getRobocodeFrame(), "Import failed: " + e);
                }
            }
        }
    }
