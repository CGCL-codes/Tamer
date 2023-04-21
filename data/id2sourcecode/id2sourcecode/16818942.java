    public void rename() {
        try {
            ArgsParser ap = this.getArgsParser();
            if (checkParams()) {
                FileOutputStream fos = null;
                ObjectOutputStream out = null;
                String name = ap.getClassName();
                if (name == null || name.length() == 0 || name.equalsIgnoreCase("null")) {
                    name = "NewConfig";
                }
                while (true) {
                    DialogConfName dial = new DialogConfName(rootFrame, "Configuration Name", name);
                    dial.pack();
                    dial.setLocationRelativeTo(rootFrame);
                    dial.setVisible(true);
                    String prefix = null;
                    prefix = Category.explain(this.category);
                    name = dial.getTyped_name();
                    if (name == null) {
                        return;
                    } else if (name.equalsIgnoreCase("null")) {
                        AdminComponent.showFatalError(rootFrame, "Wrong config name.");
                    } else {
                        String filename = SaadaDB.getRoot_dir() + Database.getSepar() + "config" + Database.getSepar() + prefix + "." + name + ".config";
                        if ((new File(filename)).exists() && AdminComponent.showConfirmDialog(rootFrame, "Loader configuration <" + name + "> for \"" + prefix + "\" already exists.\nOverwrite it?") == false) {
                            ap.setName(name);
                        } else {
                            ap.setName(name);
                            this.setConfName(name);
                            fos = new FileOutputStream(SaadaDB.getRoot_dir() + Database.getSepar() + "config" + Database.getSepar() + prefix + "." + name + ".config");
                            out = new ObjectOutputStream(fos);
                            out.writeObject(ap);
                            out.close();
                            if (this.ancestor.equals(DATA_LOADER)) {
                                rootFrame.activePanel(DATA_LOADER);
                                ((DataLoaderPanel) (rootFrame.getActivePanel())).setConfig(confName);
                            }
                            return;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Messenger.printStackTrace(ex);
            AdminComponent.showFatalError(this, ex);
            return;
        }
    }
