    public boolean saveCurrent() {
        File openedFile = editor.getOpenedFile();
        Writer wr;
        boolean oldFile = true;
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            if (openedFile != null) {
                wr = new FileWriter(openedFile);
            } else {
                int option;
                do {
                    option = JOptionPane.OK_OPTION;
                    String name = JOptionPane.showInputDialog(HTMLView.this, "Enter the note name...", "Save Note", JOptionPane.OK_CANCEL_OPTION);
                    if (name == null || name.equals("")) return true;
                    File saveInFolder = bPanel.getFolder();
                    openedFile = new File(saveInFolder, name + ".html");
                    if (openedFile.exists()) {
                        option = JOptionPane.showConfirmDialog(HTMLView.this, "A note with the name - '" + name + "' already exists. Overwrite?", "Save Note", JOptionPane.YES_NO_CANCEL_OPTION);
                        if (option == JOptionPane.CANCEL_OPTION) return true;
                    } else {
                        oldFile = false;
                    }
                } while (option != JOptionPane.YES_OPTION);
                openedFile.createNewFile();
                wr = new FileWriter(openedFile);
            }
            editor.getEditorKit().write(wr, editor.getDocument(), 0, editor.getDocument().getLength());
            wr.close();
            IndexModifier modifier = getIndexModifier();
            if (oldFile) {
                deleteFromIndex(openedFile, modifier);
            }
            addToIndex(openedFile, modifier);
            modifier.flush();
            modifier.close();
            editor.setOpenedFile(openedFile);
            editor.resetModified();
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
        refresh(openedFile);
        return false;
    }
