    public void actionPerformed(ActionEvent e) {
        if (JFtp.uiBlocked) {
            return;
        }
        if (e.getActionCommand().equals("rm")) {
            lock(false);
            if (Settings.getAskToDelete()) {
                if (!UITool.askToDelete(this)) {
                    unlock(false);
                    return;
                }
            }
            for (int i = 0; i < length; i++) {
                if (dirEntry[i].selected) {
                    con.removeFileOrDir(dirEntry[i].file);
                }
            }
            unlock(false);
            fresh();
        } else if (e.getActionCommand().equals("mkdir")) {
            Creator c = new Creator("Create:", con);
        } else if (e.getActionCommand().equals("cmd")) {
            RemoteCommand rc = new RemoteCommand();
        } else if (e.getActionCommand().equals("cd")) {
            String tmp = UITool.getPathFromDialog(path);
            chdir(tmp);
        } else if (e.getActionCommand().equals("fresh")) {
            fresh();
        } else if (e.getActionCommand().equals("cp")) {
            Object[] o = jl.getSelectedValues();
            if (o == null) {
                return;
            }
            String tmp = UITool.getPathFromDialog(path);
            if (tmp == null) {
                return;
            }
            if (!tmp.endsWith("/")) {
                tmp = tmp + "/";
            }
            try {
                copy(o, path, "", tmp);
                Log.debug("Copy finished...");
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.debug("Copy failed!");
            }
        } else if (e.getActionCommand().equals("zip")) {
            try {
                Object[] entry = jl.getSelectedValues();
                if (entry == null) {
                    return;
                }
                String[] tmp = new String[entry.length];
                for (int i = 0; i < tmp.length; i++) {
                    tmp[i] = entry[i].toString();
                }
                NameChooser n = new NameChooser();
                String name = n.text.getText();
                ZipFileCreator z = new ZipFileCreator(tmp, path, name);
                fresh();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getActionCommand().equals("->")) {
            blockedTransfer(-2);
        } else if (e.getActionCommand().equals("<-")) {
            blockedTransfer(-2);
        } else if (e.getActionCommand().equals("rn")) {
            Object[] target = jl.getSelectedValues();
            if ((target == null) || (target.length == 0)) {
                Log.debug("No file selected");
                return;
            } else if (target.length > 1) {
                Log.debug("Too many files selected");
                return;
            }
            String val = JOptionPane.showInternalInputDialog(JFtp.desktop, "Choose a name...");
            if (val != null) {
                if (!con.rename(target[0].toString(), val)) {
                    Log.debug("Rename failed.");
                } else {
                    Log.debug("Successfully renamed.");
                    fresh();
                }
            }
        } else if (e.getSource() == props) {
            JFtp.statusP.jftp.clearLog();
            int x = currentPopup.getPermission();
            String tmp;
            if (x == FtpConnection.R) {
                tmp = "read only";
            } else if (x == FtpConnection.W) {
                tmp = "read/write";
            } else if (x == FtpConnection.DENIED) {
                tmp = "denied";
            } else {
                tmp = "undefined";
            }
            String msg = "File: " + currentPopup.toString() + "\n" + " Size: " + currentPopup.getFileSize() + " raw size: " + currentPopup.getRawSize() + "\n" + " Symlink: " + currentPopup.isLink() + "\n" + " Directory: " + currentPopup.isDirectory() + "\n" + " Permission: " + tmp + "\n";
            Log.debug(msg);
        } else if (e.getSource() == viewFile) {
            if (currentPopup.isDirectory()) {
                Log.debug("This is a directory, not a file.");
                return;
            }
            String url = JFtp.localDir.getPath() + currentPopup.toString();
            showContentWindow(url, currentPopup);
        } else if (e.getSource() == runFile) {
            if (currentPopup.isDirectory()) {
                Log.debug("This is a directory, not a file.");
                return;
            }
            String url = JFtp.localDir.getPath() + currentPopup.toString();
            showContentWindow("popup-run@" + url, currentPopup);
        } else if (e.getSource() == sorter) {
            sortMode = (String) sorter.getSelectedItem();
            if (sortMode.equals("Date")) {
                Settings.showLocalDateNoSize = true;
            } else {
                Settings.showLocalDateNoSize = false;
            }
            fresh();
        } else if (e.getActionCommand().equals("cdUp")) {
            chdir("..");
        }
    }
