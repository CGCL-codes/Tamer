    @SuppressWarnings("nls")
    private boolean writeFileAttachment(final String sFileName, final StringBuilder out) {
        String sRealFile = sFileName;
        final File f = new File(sRealFile);
        if (!f.exists() || !f.isFile() || !f.canRead()) {
            Log.log(Log.WARNING, "lazyj.mail.Sendmail", "writeFileAttachment : can't read from : " + sRealFile);
            this.iSentOk = SENT_ERROR;
            this.sError = "cannot attach : " + sFileName;
            return false;
        }
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(sRealFile));
        } catch (IOException e) {
            Log.log(Log.ERROR, "lazyj.mail.Sendmail", "writeFileAttachment" + e);
            this.iSentOk = SENT_ERROR;
            this.sError = "exception while attaching `" + sFileName + "` : " + e.getMessage();
            return false;
        }
        final boolean b = writeAttachment(in, sFileName, out);
        try {
            in.close();
        } catch (IOException e) {
        }
        return b;
    }
