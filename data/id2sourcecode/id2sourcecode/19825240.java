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
            if (!(con instanceof FtpConnection)) {
                Log.debug("This feature is for ftp only.");
                return;
            }
            RemoteCommand rc = new RemoteCommand();
        } else if (e.getActionCommand().equals("cd")) {
            PathChanger pthc = new PathChanger("remote");
        } else if (e.getActionCommand().equals("fresh")) {
            fresh();
        } else if (e.getActionCommand().equals("->")) {
            blockedTransfer(-2);
        } else if (e.getActionCommand().equals("<-")) {
            blockedTransfer(-2);
        } else if (e.getActionCommand().equals("list")) {
            try {
                if (!(con instanceof FtpConnection)) {
                    Log.debug("Can only list FtpConnection output!");
                }
                PrintStream out = new PrintStream(Settings.ls_out);
                for (int i = 0; i < ((FtpConnection) con).currentListing.size(); i++) {
                    out.println(((FtpConnection) con).currentListing.get(i));
                }
                out.flush();
                out.close();
                java.net.URL url = new java.io.File(Settings.ls_out).toURL();
                Displayer d = new Displayer(url);
                JFtp.desktop.add(d, new Integer(Integer.MAX_VALUE - 13));
            } catch (java.net.MalformedURLException ex) {
                ex.printStackTrace();
                Log.debug("ERROR: Malformed URL!");
            } catch (FileNotFoundException ex2) {
                ex2.printStackTrace();
                Log.debug("ERROR: File not found!");
            }
        } else if (e.getActionCommand().equals("type") && (!JFtp.uiBlocked)) {
            if (!(con instanceof FtpConnection)) {
                Log.debug("You can only set the transfer type for ftp connections.");
                return;
            }
            FtpConnection c = (FtpConnection) con;
            String t = c.getTypeNow();
            boolean ret = false;
            if (t.equals(FtpConnection.ASCII)) {
                ret = c.type(FtpConnection.BINARY);
            } else if (t.equals(FtpConnection.BINARY)) {
                ret = c.type(FtpConnection.EBCDIC);
            }
            if (t.equals(FtpConnection.EBCDIC) || (!ret && !t.equals(FtpConnection.L8))) {
                ret = c.type(FtpConnection.L8);
            }
            if (!ret) {
                c.type(FtpConnection.ASCII);
                Log.debug("Warning: type should be \"I\" if you want to transfer binary files!");
            }
            Log.debug("Type is now " + c.getTypeNow());
        } else if (e.getActionCommand().equals("que")) {
            if (!(con instanceof FtpConnection)) {
                Log.debug("Queue supported only for FTP");
                return;
            }
            Object[] o = jl.getSelectedValues();
            DirEntry[] tmp = new DirEntry[Array.getLength(o)];
            for (int i = 0; i < Array.getLength(o); i++) {
                tmp[i] = (DirEntry) o[i];
                JFtp.dQueue.addFtp(tmp[i].toString());
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
        } else if (e.getSource() == sorter) {
            sortMode = (String) sorter.getSelectedItem();
            if (sortMode.equals("Date")) {
                Settings.showDateNoSize = true;
            } else {
                Settings.showDateNoSize = false;
            }
            fresh();
        } else if (e.getActionCommand().equals("cdUp")) {
            JFtp.remoteDir.getCon().chdir("..");
        } else if (e.getActionCommand().equals("rn")) {
            Object[] target = jl.getSelectedValues();
            if ((target == null) || (target.length == 0)) {
                Log.debug("No file selected");
                return;
            } else if (target.length > 1) {
                Log.debug("Too many files selected");
                return;
            }
            String val = JOptionPane.showInternalInputDialog(this, "Choose a name...");
            if (val != null) {
                if (!con.rename(target[0].toString(), val)) {
                    Log.debug("Rename failed.");
                } else {
                    Log.debug("Successfully renamed.");
                    fresh();
                }
            }
        }
    }
