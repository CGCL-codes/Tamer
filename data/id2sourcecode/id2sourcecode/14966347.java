    private boolean fetchAndInstallDict(Dictionary d, ProgressObserver po) {
        File f = null;
        try {
            f = File.createTempFile(d.archiveName, "tmp");
        } catch (IOException ioe) {
            System.out.println("Unable to create temp file :" + ioe);
            return false;
        }
        Date modifdate = null;
        po.setStatus(jEdit.getProperty("spell-check-hunspell-download"));
        try {
            URL url = new URL(jEdit.getProperty(OOO_DICTS_PROP) + d.archiveName + ".zip");
            URLConnection connect = url.openConnection();
            connect.connect();
            po.setMaximum(connect.getContentLength());
            po.setValue(0);
            modifdate = new Date(connect.getLastModified());
            InputStream is = connect.getInputStream();
            OutputStream os = new FileOutputStream(f);
            boolean copied = IOUtilities.copyStream(po, is, os, true);
            IOUtilities.closeQuietly(os);
            if (!copied) {
                po.setStatus(jEdit.getProperty("spell-check-hunspell-error"));
                return false;
            }
        } catch (IOException ioe) {
            Log.log(Log.ERROR, HunspellDictsManager.class, "Download of " + d.getKey() + " failed");
            ioe.printStackTrace();
            GUIUtilities.error(null, "spell-check-hunspell-download.error", new String[] { ioe.getMessage() });
            return false;
        }
        Log.log(Log.DEBUG, HunspellDictsManager.class, "Download of " + d.archiveName + ".zip finished");
        return doInstallDict(d, po, f, modifdate);
    }
