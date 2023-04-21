    protected void saveAs() {
        try {
            ArgsParser ap = this.getArgsParser();
            FileOutputStream fos = null;
            ObjectOutputStream out = null;
            String name = ap.getClassName();
            if (name == null || name.length() == 0 || name.equalsIgnoreCase("null")) {
                name = "NewConfig";
            }
            while (1 == 1) {
                DialogConfName dial = new DialogConfName(this.frame.frame, "Configuration Name", name);
                dial.pack();
                dial.setLocationRelativeTo(this.frame);
                dial.setVisible(true);
                String prefix;
                prefix = Category.explain(this.category);
                name = name = dial.getTyped_name();
                if (name == null) {
                    return;
                } else if (name.equalsIgnoreCase("null")) {
                    SaadaDBAdmin.showFatalError(this.frame, "Wrong config name.");
                } else {
                    String filename = SaadaDB.getRoot_dir() + Database.getSepar() + "config" + Database.getSepar() + prefix + "." + name + ".config";
                    if ((new File(filename)).exists() && SaadaDBAdmin.showConfirmDialog(this.frame, "Loader configuration <" + name + "> for \"" + prefix + "\" already exists.\nOverwrite it?") == false) {
                        ap.setName(name);
                    } else {
                        ap.setName(name);
                        this.setConf_name(name);
                        fos = new FileOutputStream(SaadaDB.getRoot_dir() + Database.getSepar() + "config" + Database.getSepar() + prefix + "." + name + ".config");
                        out = new ObjectOutputStream(fos);
                        out.writeObject(ap);
                        out.close();
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            SaadaDBAdmin.showFatalError(this, ex.toString());
            return;
        }
    }
