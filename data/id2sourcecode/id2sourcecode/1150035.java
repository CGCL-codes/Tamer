        public void actionPerformed(ActionEvent evt) {
            Executable executable = new Executable() {

                public void execute() {
                    interrupted = false;
                    try {
                        MainFrame.lockGUI("Importing...");
                        File sourceDir = new File(sourceDirTField.getText());
                        if (sourceDir.isDirectory()) {
                            Corpus corpus = Factory.newCorpus(corpusNameTField.getText());
                            String textExt = textExtensionTField.getText();
                            String keyExt = keyExtensionTField.getText();
                            String encoding = encodingTField.getText();
                            ExtensionFileFilter filter = new ExtensionFileFilter();
                            filter.addExtension(textExt);
                            File[] textFiles = sourceDir.listFiles(filter);
                            if (textFiles != null) {
                                for (int i = 0; i < textFiles.length; i++) {
                                    if (textFiles[i].isDirectory()) continue;
                                    FeatureMap params = Factory.newFeatureMap();
                                    params.put("sourceUrl", textFiles[i].toURI().toURL());
                                    if (encoding != null && encoding.length() > 0) params.put("encoding", encoding);
                                    Document doc = (Document) Factory.createResource("gate.corpora.DocumentImpl", params, null, textFiles[i].getName());
                                    corpus.add(doc);
                                    String fileName = textFiles[i].getCanonicalPath();
                                    int pos = fileName.lastIndexOf(textExt);
                                    fileName = fileName.substring(0, pos) + keyExt;
                                    List keys = new ArrayList();
                                    Exception e = null;
                                    try {
                                        BufferedReader reader;
                                        if (encoding != null && encoding.length() > 0) {
                                            reader = new BomStrippingInputStreamReader(new FileInputStream(fileName), encoding);
                                        } else {
                                            reader = new BufferedReader(new FileReader(fileName));
                                        }
                                        String line = reader.readLine();
                                        while (line != null) {
                                            keys.add(line);
                                            line = reader.readLine();
                                        }
                                    } catch (IOException ioe) {
                                        e = ioe;
                                    }
                                    if (keys.isEmpty() || e != null) {
                                        MainFrame.unlockGUI();
                                        int res = JOptionPane.showOptionDialog(CorpusImporter.this, "There were problems obtainig the keyphrases for " + textFiles[i].getName() + "!", "Gate", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[] { "Continue", "Stop importing" }, "Continue");
                                        if (e != null) e.printStackTrace(Err.getPrintWriter());
                                        if (res == JOptionPane.NO_OPTION) {
                                            interrupt();
                                        } else {
                                            MainFrame.lockGUI("Importing...");
                                        }
                                        Factory.deleteResource(doc);
                                    } else {
                                        if (!annotateKeyPhrases(doc, annotationSetTField.getText(), annotationTypeTField.getText(), keys)) {
                                            MainFrame.unlockGUI();
                                            int res = JOptionPane.showOptionDialog(CorpusImporter.this, "None of the keyphrases were present in " + textFiles[i].getName(), "Gate", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[] { "Keep document", "Discard document", "Stop importing" }, "Discard document");
                                            if (res == JOptionPane.CANCEL_OPTION) {
                                                interrupt();
                                            } else {
                                                MainFrame.lockGUI("Importing...");
                                                if (res == JOptionPane.NO_OPTION) {
                                                    Factory.deleteResource(doc);
                                                }
                                            }
                                        }
                                    }
                                    if (isInterrupted()) {
                                        MainFrame.unlockGUI();
                                        return;
                                    }
                                }
                            }
                        } else {
                            throw new Exception(sourceDir.getCanonicalPath() + " is not a directory!");
                        }
                    } catch (Exception e) {
                        MainFrame.unlockGUI();
                        JOptionPane.showMessageDialog(MainFrame.getInstance(), "Error!\n" + e.toString(), "Gate", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace(Err.getPrintWriter());
                    } finally {
                        MainFrame.unlockGUI();
                        Gate.setExecutable(null);
                    }
                }

                public synchronized boolean isInterrupted() {
                    return interrupted;
                }

                public synchronized void interrupt() {
                    interrupted = true;
                }

                protected boolean interrupted = false;
            };
            class Executor implements Runnable {

                public Executor(Executable executable) {
                    this.executable = executable;
                }

                public void run() {
                    try {
                        executable.execute();
                    } catch (ExecutionException ee) {
                        MainFrame.unlockGUI();
                        JOptionPane.showMessageDialog(MainFrame.getInstance(), "Error!\n" + ee.toString(), "Gate", JOptionPane.ERROR_MESSAGE);
                        ee.printStackTrace(Err.getPrintWriter());
                    }
                }

                Executable executable;
            }
            ;
            Thread thread = new Thread(new Executor(executable));
            thread.setPriority(Thread.MIN_PRIORITY);
            Gate.setExecutable(executable);
            thread.start();
        }
