    protected boolean isBizDriverPanelValid() {
        String fname = fileChooser.getFileString().trim();
        fileChooser.getTextField().selectAll();
        if (fname.equals("")) {
            ControlFactory.showMessageDialog(t.getString("Enter BizDriver file name."), t.getString("Information"));
            return false;
        }
        File bizDriverFile = ResourceUtils.getFile(fname);
        if (bizDriverFile.isDirectory()) {
            ControlFactory.showMessageDialog(t.getString("Enter BizDriver file name."), t.getString("Information"));
            return false;
        }
        if (bizDriverFile.getName().trim().equals(".xdr")) {
            ControlFactory.showMessageDialog(t.getString("Enter BizDriver file name."), t.getString("Information"));
            return false;
        }
        if ((bizDriverFile.getAbsolutePath().indexOf('#') > -1) || (bizDriverFile.getAbsolutePath().indexOf('%') > -1)) {
            ControlFactory.showMessageDialog(t.getString("Please do not enter # or % in the file path or name."), t.getString("Information"));
            return false;
        }
        if (urlCombo.getText().trim().equals("")) {
            ControlFactory.showMessageDialog(t.getString("Please enter a URL."), t.getString("Information"));
            return false;
        }
        String dirName = bizDriverFile.getParent();
        if ((dirName == null) || dirName.trim().equals("")) {
            dirName = Path.fromOSString(initPath).addTrailingSeparator().toString();
            fileChooser.setFileString(dirName + fileChooser.getFileString());
            fileChooser.getTextField().selectAll();
        } else if (dirName.trim().equals(File.separator)) {
            dirName = initPath;
            fileChooser.setFileString(dirName.substring(0, dirName.length() - 1) + fileChooser.getFileString());
            fileChooser.getTextField().selectAll();
        } else {
            dirName = dirName.trim();
            if (Path.fromOSString(dirName).hasTrailingSeparator()) {
                fileChooser.setFileString(dirName + bizDriverFile.getName().trim());
                fileChooser.getTextField().selectAll();
            } else {
                fileChooser.getTextField().selectAll();
            }
        }
        final File tmpFile = ResourceUtils.getDirectory(dirName);
        if (!tmpFile.isDirectory()) {
            ControlFactory.showMessageDialog(t.getString("Invalid directory for BizDriver file."), t.getString("Information"));
            return false;
        }
        fname = fileChooser.getFileString().trim();
        bizDriverFile = ResourceUtils.getFile(fname);
        if (createNewBtn.getSelection()) {
            final String fileNameStr = bizDriverFile.getName();
            if (fileNameStr.indexOf('.') == -1) {
                fname = fname + ".xdr";
                fileChooser.setFileString(fname);
                fileChooser.getTextField().selectAll();
                bizDriverFile = new File(fname);
            } else if (fileNameStr.indexOf('.') == (fileNameStr.length() - 1)) {
                fname = fname + "xdr";
                fileChooser.setFileString(fname);
                fileChooser.getTextField().selectAll();
                bizDriverFile = new File(fname);
            }
            if (bizDriverFile.exists()) {
                final int choice = ControlFactory.showConfirmDialog("BizDriver file which you have entered already exists. Overwrite?");
                if (choice != MessageDialog.OK) {
                    return false;
                }
            }
        } else if (useExistingBtn.getSelection()) {
            if (bizDriverFile.isDirectory() || !bizDriverFile.exists()) {
                ControlFactory.showMessageDialog(t.getString("BizDriver file which you have entered does not exist. Please enter correct file name."), t.getString("Information"));
                return false;
            }
        }
        return true;
    }
