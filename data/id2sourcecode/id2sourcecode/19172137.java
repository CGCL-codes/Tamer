    private void savePuzzle() {
        fileChooser.setFileFilter(puzzleFileFilter);
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File pFile = fileChooser.getSelectedFile();
            if (!pFile.getName().endsWith(".ser")) pFile = new File(pFile.getParentFile(), pFile.getName() + ".ser");
            boolean goOn = true;
            if (pFile.exists()) goOn = JOptionPane.showConfirmDialog(this, "The file " + pFile.getName() + " already exists, overwrite?", "File already exists", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
            if (goOn) {
                try {
                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(pFile));
                    try {
                        out.writeObject(puzzle);
                    } finally {
                        out.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                }
            }
        }
    }
