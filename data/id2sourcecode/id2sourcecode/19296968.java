    @Override
    public void importNotesFromServer() {
        boolean downloaded = true;
        try {
            makeBackupFile();
            File f = new File(UserSettings.getInstance().getNotesFile());
            FileOutputStream fos = new FileOutputStream(f);
            String filename = f.getName();
            String ftpString = "ftp://" + UserSettings.getInstance().getServerUser() + ":" + UserSettings.getInstance().getServerPasswdString() + "@" + UserSettings.getInstance().getServerAddress() + UserSettings.getInstance().getServerDir() + filename + ";type=i";
            URL url = new URL(ftpString);
            URLConnection urlc = url.openConnection();
            InputStream is = urlc.getInputStream();
            int nextByte = is.read();
            while (nextByte != -1) {
                fos.write(nextByte);
                nextByte = is.read();
            }
            fos.close();
        } catch (IOException e) {
            downloaded = false;
        }
        if (downloaded) {
            deleteBackupFile();
            JOptionPane.showMessageDialog(null, I18N.getInstance().getString("info.notesfiledownloaded"), I18N.getInstance().getString("info.title"), JOptionPane.INFORMATION_MESSAGE);
        } else {
            restoreFileFromBackup();
            JOptionPane.showMessageDialog(null, I18N.getInstance().getString("error.notesfilenotdownloaded"), I18N.getInstance().getString("error.title"), JOptionPane.ERROR_MESSAGE);
        }
    }
