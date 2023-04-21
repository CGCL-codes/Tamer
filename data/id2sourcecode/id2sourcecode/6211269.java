    private void sendFile(File f, PrintStream out) throws IOException {
        java.io.InputStream is = null;
        out.write(EOL);
        if (f.exists()) {
            try {
                is = new java.io.FileInputStream(f.getAbsolutePath());
                byte[] buf = new byte[4096];
                while (is.available() > 0) {
                    int readed = is.read(buf);
                    out.write(buf, 0, readed);
                }
            } catch (Exception e) {
                this.bot.log(ILogger.LogLevel.Error, "WWW sendFile() Error:", e);
            } finally {
                is.close();
            }
        }
    }
