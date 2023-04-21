    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("SUBMIT")) {
            if (storeName.getText().equals("")) {
                JOptionPane.showInternalMessageDialog(this, "Store Name must be filled in!", "Incomplete Application Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (EngineDriver.getSystemUser() instanceof StoreOwner) {
                    Store newStore = new Store((StoreOwner) EngineDriver.getSystemUser(), storeName.getText());
                    if (imageFile != null) {
                        if (FileUtils.checkImgEncomDirectory(imageFile)) {
                            newStore.setIcon(imageFile);
                        } else {
                            try {
                                File token = new File("ENCOM/images/" + imageFile.getName());
                                FileUtils.copyFile(imageFile, token);
                                newStore.setIcon(token);
                            } catch (IOException ex) {
                                Logger.getLogger(NewStoreApp.class.getPackage().getName()).log(Level.WARNING, "IO ERROR SETTING UP ICON", ex);
                            }
                        }
                    }
                    StoreApplication storeApp = new StoreApplication((StoreOwner) EngineDriver.getSystemUser(), newStore, storeDescription.getText());
                    EngineDriver.saveStoreApplicationTo_DB(storeApp);
                    EngineDriver.storeOwnerUI((StoreOwner) EngineDriver.getSystemUser());
                    this.dispose();
                } else if (EngineDriver.getSystemUser() instanceof Customer) {
                    Store newStore = new Store((Customer) EngineDriver.getSystemUser(), storeName.getText());
                    if (imageFile != null) {
                        if (FileUtils.checkImgEncomDirectory(imageFile)) {
                            newStore.setIcon(imageFile);
                        } else {
                            try {
                                File token = new File("ENCOM/images/" + imageFile.getName());
                                FileUtils.copyFile(imageFile, token);
                                newStore.setIcon(token);
                            } catch (IOException ex) {
                                Logger.getLogger(NewStoreApp.class.getPackage().getName()).log(Level.WARNING, "ERROR: Image not transfered during application process.", ex);
                            }
                        }
                    }
                    StoreApplication storeApp = new StoreApplication((Customer) EngineDriver.getSystemUser(), newStore, storeDescription.getText());
                    EngineDriver.saveStoreApplicationTo_DB(storeApp);
                    EngineDriver.mallUI();
                    this.dispose();
                } else if (EngineDriver.isMasterAdmin()) {
                }
            }
        } else if (e.getActionCommand().equals("CANCEL")) {
            EngineDriver.mallUI();
            this.dispose();
        }
    }
