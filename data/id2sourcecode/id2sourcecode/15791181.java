    void handleSave() {
        String newName = name.getText();
        boolean toRename = oldName.length() > 0 && !newName.equals(oldName);
        boolean toCreate = oldName.length() <= 0 && newName.length() > 0;
        if (toRename || toCreate) {
            MetaProject mp = MDEPlugin.getDefault().getRuntime().getMetaSolution().getMetaProject(newName);
            if (mp != null) {
                if (MessageDialog.openConfirm(this.getShell(), this.getShell().getText(), "MetaProject [" + newName + "] already exists. Choose <OK> to overwrite it.")) {
                    MDEPlugin.getDefault().getRuntime().getMetaSolution().removeMetaProject(mp);
                } else {
                    this.handleCancel();
                    return;
                }
            }
        }
        this.populateServerObjects();
        if (toCreate) {
            java.util.Collection mprojs = MDEPlugin.getDefault().getRuntime().getMetaSolution().getAllMetaProjects();
            if (!mprojs.contains(mproj)) mprojs.add(this.mproj);
        }
        String err = null;
        try {
            MDEPlugin.getDefault().getRuntime().saveMetaSolution();
            if (toRename) {
                this.firePropertyChange("", oldName, this.getMproj());
            }
        } catch (IOException e) {
            err = e.getMessage();
        }
        this.close();
        if (err != null) {
            if (toRename) {
                mproj.setName(oldName);
            } else if (toCreate) {
                MDEPlugin.getDefault().getRuntime().getMetaSolution().removeMetaProject(mproj);
            }
            MDEPlugin.showMessage(err);
        }
    }
