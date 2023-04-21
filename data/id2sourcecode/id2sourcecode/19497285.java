        public void run() {
            Stack directoryStack = new Stack();
            directoryStack.push(classUrl);
            Filesystem system = classUrl.getFilesystem();
            ClassWriter classwriter = new ClassWriter();
            final long startTime = System.currentTimeMillis();
            try {
                while (!directoryStack.isEmpty()) {
                    Url currentDirectory = (Url) directoryStack.pop();
                    Url[] contents = system.getChildren(currentDirectory, null, Filesystem.TYPE_BOTH);
                    for (int i = 0; i < contents.length; i++) {
                        if (system.isDirectory(contents[i])) directoryStack.push(contents[i]); else {
                            if (contents[i].getFileExtension().equals("class") && system.getLastModified(contents[i]) > lastModified) {
                                System.out.println("Analyzing " + contents[i].toString());
                                BufferedInputStream bis = new BufferedInputStream(system.getInputStream(contents[i]));
                                classwriter.readClass(bis);
                                DBClass.addClassToDB(classwriter, db);
                                bis.close();
                            }
                        }
                    }
                }
                db.closeDB();
                db.openDB(new File(goodPath));
                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        BBQPathsGroup.updateTimeProperty.setValue(node, Long.toString(startTime));
                    }
                });
            } catch (Exception ioe) {
                ioe.printStackTrace();
            } finally {
                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        button.setEnabled(true);
                    }
                });
            }
        }
