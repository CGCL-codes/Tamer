        public void actionPerformed(ActionEvent e) {
            Runnable runnable = new Runnable() {

                public void run() {
                    try {
                        XJFileChooser fileChooser = MainFrame.getFileChooser();
                        fileChooser.setDialogTitle("Select the directory that will contain the corpus");
                        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        if (fileChooser.showDialog(getLargeView() != null ? getLargeView() : getSmallView(), "Select") == JFileChooser.APPROVE_OPTION) {
                            File dir = fileChooser.getSelectedFile();
                            if (!dir.exists()) {
                                if (!dir.mkdirs()) {
                                    JOptionPane.showMessageDialog(largeView != null ? largeView : smallView, "Could not create top directory!", "GATE", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }
                            MainFrame.lockGUI("Saving...");
                            Corpus corpus = (Corpus) target;
                            Iterator<Document> docIter = corpus.iterator();
                            boolean overwriteAll = false;
                            int docCnt = corpus.size();
                            int currentDocIndex = 0;
                            Set<String> usedFileNames = new HashSet<String>();
                            while (docIter.hasNext()) {
                                boolean docWasLoaded = corpus.isDocumentLoaded(currentDocIndex);
                                Document currentDoc = (Document) docIter.next();
                                URL sourceURL = currentDoc.getSourceUrl();
                                String fileName = null;
                                if (sourceURL != null) {
                                    fileName = sourceURL.getPath();
                                    fileName = Files.getLastPathComponent(fileName);
                                }
                                if (fileName == null || fileName.length() == 0) {
                                    fileName = currentDoc.getName();
                                }
                                fileName = fileName.replaceAll("[\\/:\\*\\?\"<>|]", "_");
                                if (fileName.toLowerCase().endsWith(".xml")) {
                                    fileName = fileName.substring(0, fileName.length() - 4);
                                }
                                if (usedFileNames.contains(fileName)) {
                                    String fileNameBase = fileName;
                                    int uniqId = 0;
                                    fileName = fileNameBase + "-" + uniqId++;
                                    while (usedFileNames.contains(fileName)) {
                                        fileName = fileNameBase + "-" + uniqId++;
                                    }
                                }
                                usedFileNames.add(fileName);
                                if (!fileName.toLowerCase().endsWith(".xml")) fileName += ".xml";
                                File docFile = null;
                                boolean nameOK = false;
                                do {
                                    docFile = new File(dir, fileName);
                                    if (docFile.exists() && !overwriteAll) {
                                        Object[] options = new Object[] { "Yes", "All", "No", "Cancel" };
                                        MainFrame.unlockGUI();
                                        int answer = JOptionPane.showOptionDialog(largeView != null ? largeView : smallView, "File " + docFile.getName() + " already exists!\n" + "Overwrite?", "GATE", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[2]);
                                        MainFrame.lockGUI("Saving...");
                                        switch(answer) {
                                            case 0:
                                                {
                                                    nameOK = true;
                                                    break;
                                                }
                                            case 1:
                                                {
                                                    nameOK = true;
                                                    overwriteAll = true;
                                                    break;
                                                }
                                            case 2:
                                                {
                                                    MainFrame.unlockGUI();
                                                    fileName = (String) JOptionPane.showInputDialog(largeView != null ? largeView : smallView, "Please provide an alternative file name", "GATE", JOptionPane.QUESTION_MESSAGE, null, null, fileName);
                                                    if (fileName == null) {
                                                        fireProcessFinished();
                                                        return;
                                                    }
                                                    MainFrame.lockGUI("Saving");
                                                    break;
                                                }
                                            case 3:
                                                {
                                                    fireProcessFinished();
                                                    return;
                                                }
                                        }
                                    } else {
                                        nameOK = true;
                                    }
                                } while (!nameOK);
                                try {
                                    DocumentStaxUtils.writeDocument(currentDoc, docFile);
                                } catch (Exception ioe) {
                                    MainFrame.unlockGUI();
                                    JOptionPane.showMessageDialog(largeView != null ? largeView : smallView, "Could not create write file:" + ioe.toString(), "GATE", JOptionPane.ERROR_MESSAGE);
                                    ioe.printStackTrace(Err.getPrintWriter());
                                    return;
                                }
                                fireStatusChanged(currentDoc.getName() + " saved");
                                if (!docWasLoaded) {
                                    corpus.unloadDocument(currentDoc);
                                    Factory.deleteResource(currentDoc);
                                }
                                fireProgressChanged(100 * currentDocIndex++ / docCnt);
                            }
                            fireStatusChanged("Corpus saved");
                            fireProcessFinished();
                        }
                    } finally {
                        MainFrame.unlockGUI();
                    }
                }
            };
            Thread thread = new Thread(Thread.currentThread().getThreadGroup(), runnable, "Corpus XML dumper");
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        }
