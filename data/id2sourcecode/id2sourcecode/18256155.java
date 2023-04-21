        public void setValueAt(Object value, final int row, int col) {
            if (col == COLUMN_SAVE) {
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showSaveDialog(GUI.getTopParentContainer());
                if (returnVal != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                final File saveFile = fc.getSelectedFile();
                if (saveFile.exists()) {
                    String s1 = "Overwrite";
                    String s2 = "Cancel";
                    Object[] options = { s1, s2 };
                    int n = JOptionPane.showOptionDialog(GUI.getTopParentContainer(), "A file with the same name already exists.\nDo you want to remove it?", "Overwrite existing file?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, s1);
                    if (n != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
                if (saveFile.exists() && !saveFile.canWrite()) {
                    logger.fatal("No write access to file: " + saveFile);
                    return;
                }
                new Thread(new Runnable() {

                    public void run() {
                        try {
                            logger.info("Saving to file: " + saveFile.getName());
                            boolean ok = coffeeFS.extractFile(files[row].getName(), saveFile);
                            if (!ok) {
                                logger.warn("Error when saving to file: " + saveFile.getName());
                            }
                        } catch (Exception e) {
                            logger.fatal("Coffee exception: " + e.getMessage(), e);
                        }
                        updateFS();
                    }
                }).start();
                return;
            }
            if (col == COLUMN_REMOVE) {
                int reply = JOptionPane.showConfirmDialog(GUI.getTopParentContainer(), "Remove \"" + files[row].getName() + "\" from filesystem?");
                if (reply != JOptionPane.YES_OPTION) {
                    return;
                }
                new Thread(new Runnable() {

                    public void run() {
                        try {
                            logger.info("Removing file: " + files[row].getName());
                            coffeeFS.removeFile(files[row].getName());
                        } catch (Exception e) {
                            logger.fatal("Coffee exception: " + e.getMessage(), e);
                        }
                        updateFS();
                    }
                }).start();
                return;
            }
        }
