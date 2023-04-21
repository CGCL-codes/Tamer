    protected boolean isJ2EEPanelValid() {
        String fname = fileChooser.getFileString().trim();
        fileChooser.getTextField().removeModifyListener(this);
        fileChooser.setFileString(fname);
        fileChooser.getTextField().addModifyListener(this);
        if (fname.equals("")) {
            ControlFactory.showMessageDialog("Enter BizDriver file name.", "Information");
            return false;
        }
        File bizDriverFile = ResourceUtils.getFile(fname);
        if (bizDriverFile.isDirectory()) {
            ControlFactory.showMessageDialog("Enter BizDriver file name.", "Information");
            return false;
        }
        if (bizDriverFile.getName().trim().equals(".xdr")) {
            ControlFactory.showMessageDialog("Enter BizDriver file name.", "Information");
            return false;
        }
        if ((bizDriverFile.getAbsolutePath().indexOf('#') > -1) || (bizDriverFile.getAbsolutePath().indexOf('%') > -1)) {
            ControlFactory.showMessageDialog("Please do not enter # or % in the file path or name.", "Information");
            return false;
        }
        String dirName = bizDriverFile.getParent();
        if ((dirName == null) || dirName.trim().equals("")) {
            dirName = XA_Designer_Plugin.getActiveEditedFileDirectory();
            if (dirName.charAt(dirName.length() - 1) != File.separatorChar) {
                dirName = dirName + File.separator;
            }
            fileChooser.getTextField().removeModifyListener(this);
            fileChooser.setFileString(dirName + fileChooser.getFileString());
            fileChooser.getTextField().addModifyListener(this);
        } else if (dirName.trim().equals(File.separator)) {
            dirName = XA_Designer_Plugin.getActiveEditedFileDirectory();
            fileChooser.getTextField().removeModifyListener(this);
            fileChooser.setFileString(dirName.substring(0, dirName.length() - 1) + fileChooser.getFileString());
            fileChooser.getTextField().addModifyListener(this);
        } else {
            dirName = dirName.trim();
            fileChooser.getTextField().removeModifyListener(this);
            if (dirName.charAt(dirName.length() - 1) == File.separatorChar) {
                fileChooser.setFileString(dirName + bizDriverFile.getName().trim());
            } else {
                fileChooser.setFileString(fname);
            }
            fileChooser.getTextField().addModifyListener(this);
        }
        final File tmpFile = ResourceUtils.getFile(dirName);
        if (!tmpFile.isDirectory()) {
            ControlFactory.showMessageDialog("Invalid directory for BizDriver file.", "Information");
            return false;
        }
        fname = fileChooser.getFileString().trim();
        bizDriverFile = ResourceUtils.getFile(fname);
        if (createNewBtn.getSelection()) {
            final String fileNameStr = bizDriverFile.getName();
            fileChooser.getTextField().removeModifyListener(this);
            if (fileNameStr.indexOf('.') == -1) {
                fname = fname + ".xdr";
                fileChooser.setFileString(fname);
                bizDriverFile = new File(fname);
            } else if (fileNameStr.indexOf('.') == (fileNameStr.length() - 1)) {
                fname = fname + "xdr";
                fileChooser.setFileString(fname);
                bizDriverFile = new File(fname);
            }
            fileChooser.getTextField().addModifyListener(this);
            if (bizDriverFile.exists() && changesToBeSaved) {
                final int choice = ControlFactory.showConfirmDialog("BizDriver file which you have entered " + "already exists. Overwrite?");
                if (choice != MessageDialog.OK) {
                    return false;
                }
            }
        } else if (useExistingBtn.getSelection()) {
            if (bizDriverFile.isDirectory() || !bizDriverFile.exists()) {
                ControlFactory.showMessageDialog("BizDriver file which you have entered does not exist. Please " + "enter correct file name.", "Information");
                return false;
            }
        }
        if (this.j2eeJndiNameTxt.getText().trim().equals("")) {
            ControlFactory.showMessageDialog("Enter JNDI name.", "Information");
            this.j2eeJndiNameTxt.forceFocus();
            return false;
        }
        if (this.j2eeHostCmb.getText().trim().equals("")) {
            ControlFactory.showMessageDialog("Enter Host(URL).", "Information");
            this.j2eeHostCmb.forceFocus();
            return false;
        }
        if (this.j2eeFactoryCmb.getText().trim().equals("")) {
            ControlFactory.showMessageDialog("Enter Factory.", "Information");
            this.j2eeFactoryCmb.forceFocus();
            return false;
        }
        if (hasChanged() && !testConnectivity()) {
            return false;
        }
        return true;
    }
