    private boolean saveToFile(String efName, byte[] data) {
        boolean success = false;
        lt.info("called method saveToFile(efName: " + efName + ", byte[] data).", this);
        try {
            File file = getFilename(efName + ".dat", JFileChooser.SAVE_DIALOG);
            if ((new File(file.toString() + 1)).exists()) {
                int choice = JOptionPane.showConfirmDialog(null, "File exists already!\nDo wan't to overwrite it?", "Warning!", JOptionPane.YES_NO_OPTION);
                if (choice == 1) return false;
            }
            if (file != null) {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(data);
                fos.close();
                success = true;
            } else success = false;
        } catch (Exception e) {
            System.out.println(e);
        }
        return success;
    }
