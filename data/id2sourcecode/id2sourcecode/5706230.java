                                public void run() {
                                    JOptionPane.showMessageDialog(UnpackWizard.this, "Was about to write out file \"" + wrongFile.getAbsolutePath() + "\" but it already " + "exists.\nPlease [re]move existing files out of the way " + "and try again.", "File Not Found Error", JOptionPane.ERROR_MESSAGE);
                                }
