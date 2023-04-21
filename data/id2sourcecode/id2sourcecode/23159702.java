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
