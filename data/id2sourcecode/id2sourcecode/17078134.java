    @Override
    protected boolean isPanelValid() {
        int style = 0;
        style = SWT.OK | SWT.ICON_INFORMATION;
        String fname = fileNameTxt.getText().trim();
        fileNameTxt.setText(fname);
        fileNameTxt.setSelection(fileNameTxt.getCharCount());
        if (fname.equals("")) {
            ControlFactory.showMessageDialog(translator.getString("Enter BizDriver file name."), translator.getString("Information"), style);
            return false;
        }
        File bizDriverFile = ResourceUtils.getFile(fname);
        if (bizDriverFile.isDirectory()) {
            ControlFactory.showMessageDialog(translator.getString("Enter BizDriver file name."), translator.getString("Information"), style);
            return false;
        }
        if (bizDriverFile.getName().trim().equals(".xdr")) {
            ControlFactory.showMessageDialog(translator.getString("Enter BizDriver file name."), translator.getString("Information"), style);
            return false;
        }
        if ((bizDriverFile.getAbsolutePath().indexOf('#') > -1) || (bizDriverFile.getAbsolutePath().indexOf('%') > -1)) {
            ControlFactory.showMessageDialog(translator.getString("Please do not enter # or % in the file path or name."), translator.getString("Information"), style);
            return false;
        }
        String dirName = bizDriverFile.getParent();
        if ((dirName == null) || dirName.trim().equals("")) {
            dirName = XA_Designer_Plugin.getPluginRootPath();
            if (dirName.charAt(dirName.length() - 1) != File.separatorChar) {
                dirName = dirName + File.separator;
            }
            fileNameTxt.setText(dirName + fileNameTxt.getText());
            fileNameTxt.setSelection(fileNameTxt.getCharCount());
        } else if (dirName.trim().equals(File.separator)) {
            dirName = XA_Designer_Plugin.getPluginRootPath();
            fileNameTxt.setText(dirName.substring(0, dirName.length() - 1) + fileNameTxt.getText());
            fileNameTxt.setSelection(fileNameTxt.getCharCount());
        } else {
            dirName = dirName.trim();
            if (dirName.charAt(dirName.length() - 1) == File.separatorChar) {
                fileNameTxt.setText(dirName + bizDriverFile.getName().trim());
                fileNameTxt.setSelection(fileNameTxt.getCharCount());
            } else {
                fileNameTxt.setText(fname);
                fileNameTxt.setSelection(fileNameTxt.getCharCount());
            }
        }
        final File tmpFile = new File(dirName);
        if (!tmpFile.isDirectory()) {
            ControlFactory.showMessageDialog(translator.getString("Invalid directory for BizDriver file."), translator.getString("Information"), style);
            return false;
        }
        fname = fileNameTxt.getText().trim();
        bizDriverFile = ResourceUtils.getFile(fname);
        if (newRdo.getSelection()) {
            final String fileNameStr = bizDriverFile.getName();
            if (fileNameStr.indexOf('.') == -1) {
                fname = fname + ".xdr";
                fileNameTxt.setText(fname);
                fileNameTxt.setSelection(fileNameTxt.getCharCount());
                bizDriverFile = new File(fname);
            } else if (fileNameStr.indexOf('.') == (fileNameStr.length() - 1)) {
                fname = fname + "xdr";
                fileNameTxt.setText(fname);
                fileNameTxt.setSelection(fileNameTxt.getCharCount());
                bizDriverFile = new File(fname);
            }
            if (bizDriverFile.exists()) {
                final int choice = ControlFactory.showConfirmDialog(translator.getString("BizDriver file which you have entered already exists. Overwrite?"), false);
                if (choice != Window.OK) {
                    return false;
                }
            }
        } else if (existsRdo.getSelection()) {
            if (bizDriverFile.isDirectory() || !bizDriverFile.exists()) {
                ControlFactory.showMessageDialog(translator.getString("BizDriver file which you have entered does not exist. Please enter correct file name."), translator.getString("Information"), style);
                return false;
            }
        }
        return true;
    }
