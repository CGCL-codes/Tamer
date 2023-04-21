    public File runSaveAsDialog(String fileName) {
        JFileChooser fc = new JFileChooser(new File(workingDir()));
        fc.setSelectedFile(new File(fileName));
        while (true) {
            int result = fc.showSaveDialog(frame);
            if (result != JFileChooser.APPROVE_OPTION) return null;
            File file = fc.getSelectedFile();
            if (!file.exists()) return file;
            int overwriteResult = JOptionPane.showConfirmDialog(frame, "The file " + file + " already exists. Do you want to overwrite the existing file?", "Save file", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (overwriteResult == JOptionPane.YES_OPTION) return file;
            if (overwriteResult == JOptionPane.CANCEL_OPTION) return null;
        }
    }
