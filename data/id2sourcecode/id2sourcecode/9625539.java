    public static void showGenerateRTConcJUnitJarFileDialog(final Frame parentFrame, File rtFile, final File concJUnitJarFile, final Runnable1<File> successRunnable, final Runnable failureRunnable) {
        if ((rtFile == null) || (FileOps.NULL_FILE.equals(rtFile))) {
            File drJavaFile = FileOps.getDrJavaApplicationFile();
            File parent = drJavaFile.getParentFile();
            if (parent == null) {
                parent = new File(System.getProperty("user.dir"));
            }
            rtFile = new File(parent, "rt.concjunit.jar");
        }
        JFileChooser saveChooser = new JFileChooser() {

            public void setCurrentDirectory(File dir) {
                super.setCurrentDirectory(dir);
                setDialogTitle("Save:  " + getCurrentDirectory());
            }
        };
        saveChooser.setPreferredSize(new Dimension(650, 410));
        saveChooser.setSelectedFile(rtFile);
        saveChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

            public boolean accept(File f) {
                return f.isDirectory() || f.getPath().endsWith(".jar");
            }

            public String getDescription() {
                return "Java Archive Files (*.jar)";
            }
        });
        saveChooser.setMultiSelectionEnabled(false);
        int rc = saveChooser.showSaveDialog(parentFrame);
        if (rc == JFileChooser.APPROVE_OPTION) {
            final File targetFile = saveChooser.getSelectedFile();
            int n = JOptionPane.YES_OPTION;
            if (targetFile.exists()) {
                Object[] options = { "Yes", "No" };
                n = JOptionPane.showOptionDialog(parentFrame, "This file already exists.  Do you wish to overwrite the file?", "Confirm Overwrite", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            }
            if (n == JOptionPane.YES_OPTION) {
                if (parentFrame != null) parentFrame.setEnabled(false);
                final ProcessingDialog processingDialog = new ProcessingDialog(parentFrame, "Creating ConcJUnit Runtime", "Processing, please wait.");
                final JProgressBar pb = processingDialog.getProgressBar();
                processingDialog.setVisible(true);
                try {
                    final File tmpDir = FileOps.createTempDirectory("DrJavaGenerateRTConcJUnitJar");
                    SwingWorker worker = new SwingWorker() {

                        volatile Boolean _success = null;

                        Thread _processIncrementer = new Thread(new Runnable() {

                            public void run() {
                                File tmpFile = new File(tmpDir, "rt.concjunit.jar");
                                boolean indeterminate = true;
                                try {
                                    while (_success == null) {
                                        Thread.sleep(1000);
                                        if (tmpFile.exists()) {
                                            if (indeterminate) {
                                                pb.setIndeterminate(false);
                                                indeterminate = false;
                                            }
                                            pb.setValue((int) (100.0 / (30 * 1024 * 1024) * tmpFile.length()));
                                        }
                                    }
                                } catch (InterruptedException ie) {
                                    pb.setIndeterminate(true);
                                }
                            }
                        });

                        public Object construct() {
                            _processIncrementer.start();
                            _success = edu.rice.cs.drjava.model.junit.ConcJUnitUtils.generateRTConcJUnitJarFile(targetFile, concJUnitJarFile, tmpDir);
                            return null;
                        }

                        public void finished() {
                            pb.setValue(100);
                            processingDialog.setVisible(false);
                            processingDialog.dispose();
                            if (parentFrame != null) parentFrame.setEnabled(true);
                            if ((_success != null) && (_success)) {
                                successRunnable.run(targetFile);
                                JOptionPane.showMessageDialog(parentFrame, "Successfully generated ConcJUnit Runtime File:\n" + targetFile, "Generation Successful", JOptionPane.INFORMATION_MESSAGE);
                                edu.rice.cs.plt.io.IOUtil.deleteRecursively(tmpDir);
                            } else {
                                failureRunnable.run();
                                JOptionPane.showMessageDialog(parentFrame, "Could not generate ConcJUnit Runtime File:\n" + targetFile, "Could Not Generate", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    };
                    worker.start();
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(parentFrame, "Could not generate ConcJUnit Runtime file:\n" + targetFile, "Could Not Generate", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            failureRunnable.run();
        }
    }
