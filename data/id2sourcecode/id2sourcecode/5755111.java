    private boolean saveToFile(String efName, Vector records) {
        boolean success = false;
        File file = null;
        File savefile = null;
        lt.info("called method saveToFile(efName: " + efName + ", Vector records).", this);
        try {
            file = getFilename(efName + ".dat", JFileChooser.SAVE_DIALOG);
            if ((new File(file.toString() + 1)).exists()) {
                int choice = JOptionPane.showConfirmDialog(null, "File exists already!\nDo wan't to overwrite it?", "Warning!", JOptionPane.YES_NO_OPTION);
                if (choice == 1) return false;
            }
            if (file != null) {
                for (int i = 0; i < records.size(); i++) {
                    savefile = new File(file.toString() + (i + 1));
                    FileOutputStream fos = new FileOutputStream(savefile);
                    fos.write((byte[]) records.elementAt(i));
                    fos.close();
                }
                success = true;
            } else success = false;
        } catch (Exception e) {
            System.out.println(e);
        }
        return success;
    }
