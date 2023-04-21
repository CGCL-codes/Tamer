            public void load() throws Exception {
                final GUILayer layer = GUILayer.getLoadedInstance();
                final FileWindow fileWindow = FileWindow.getInstance();
                prepareFileWindow(fileWindow);
                StateManager.getGame().executeInGL(new Callable<Object>() {

                    public Object call() throws Exception {
                        fileWindow.display(layer.getDisplay(), FileMode.SAVE);
                        final FileChooserDialog chooser = fileWindow.getFileChooser();
                        chooser.addListener(new FileDialogListener() {

                            public void cancel() {
                                chooser.removeListener(this);
                                fileWindow.close();
                            }

                            public void fileSelected(File file) {
                                chooser.removeListener(this);
                                fileWindow.close();
                                if (file != null) {
                                    if (file.getName().indexOf('.') <= 0) {
                                        file = new File(file.getAbsolutePath() + "." + FileWindow.EXTENSION);
                                    } else if (file.getName().charAt(file.getName().length() - 1) == '.') {
                                        file = new File(file.getAbsolutePath() + FileWindow.EXTENSION);
                                    }
                                    final File fFile = file;
                                    StateManager.getThreadPool().execute(new Runnable() {

                                        public void run() {
                                            if (fFile.exists()) {
                                                GeneralSettings.getInstance().getLastFile().setValue(fFile.toString());
                                                if (!userConfirm("Overwrite File?", "File \"" + fFile.getName() + "\" already exists. Overwrite?")) return;
                                                final File fileOpen = fFile;
                                                GameTaskQueueManager.getManager().update(new Callable<Object>() {

                                                    public Object call() {
                                                        try {
                                                            SaveManager.getInstance().save(fileOpen, StateManager.getMachineSpace(), StateManager.getStructuralMachine());
                                                        } catch (IOException e) {
                                                            StateManager.logError(e);
                                                        }
                                                        return null;
                                                    }
                                                });
                                            } else {
                                                final File fileOpen = fFile;
                                                GameTaskQueueManager.getManager().update(new Callable<Object>() {

                                                    public Object call() {
                                                        try {
                                                            SaveManager.getInstance().save(fileOpen, StateManager.getMachineSpace(), StateManager.getStructuralMachine());
                                                        } catch (IOException e) {
                                                            StateManager.logError(e);
                                                        }
                                                        return null;
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        return null;
                    }
                });
            }
