    public boolean saveAs(File anotherFile) {
        checkFileStatus();
        try {
            if (anotherFile.exists()) {
                int answer = JOptionPane.showConfirmDialog(IDEController.get().getMainFrame(), "There already exists a file with that name. Overwrite?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) anotherFile.createNewFile(); else return false;
            }
            FileWriter fw = new FileWriter(anotherFile);
            fw.write(getEditText(), 0, getEditText().length());
            fw.close();
            m_lastModified = anotherFile.lastModified();
            renameTab(anotherFile.getName());
            setName(anotherFile.getName());
            m_id = anotherFile.getAbsolutePath();
            m_file = anotherFile;
            setAltered(false);
            IDEController.get().getStatusPanel().updateFileInfo(m_file);
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(IDEController.get().getMainFrame(), "Could not save file " + anotherFile.getName() + ".", "File error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
