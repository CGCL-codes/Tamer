    @Override
    public void exportNotesToServer() {
        boolean uploaded = true;
        String completeFilename = UserSettings.getInstance().getNotesFile();
        File f = new File(completeFilename);
        String filename = f.getName();
        String ftpString = "ftp://" + UserSettings.getInstance().getServerUser() + ":" + UserSettings.getInstance().getServerPasswdString() + "@" + UserSettings.getInstance().getServerAddress() + UserSettings.getInstance().getServerDir() + filename + ";type=i";
        FileInputStream fis;
        try {
            fis = new FileInputStream(f);
            URL url = new URL(ftpString);
            URLConnection urlc = url.openConnection();
            OutputStream os = urlc.getOutputStream();
            int nextByte = fis.read();
            while (nextByte != -1) {
                os.write(nextByte);
                nextByte = fis.read();
            }
            fis.close();
            os.close();
        } catch (IOException e) {
            uploaded = false;
            JOptionPane.showMessageDialog(null, I18N.getInstance().getString("error.notesfilenotuploaded"), I18N.getInstance().getString("error.title"), JOptionPane.ERROR_MESSAGE);
        }
        if (uploaded) {
            JOptionPane.showMessageDialog(null, I18N.getInstance().getString("info.notesfileuploaded"), I18N.getInstance().getString("info.title"), JOptionPane.INFORMATION_MESSAGE);
        }
    }
