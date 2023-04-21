    public void start() {
        ProjectManager.getInstance();
        int ver = jEdit.getIntegerProperty("p4plugin.mode_file_version", 0);
        if (MODE_FILE_VERSION > ver) {
            String dest = jEdit.getSettingsDirectory() + File.separator + "modes" + File.separator + "p4plugin.mode.xml";
            try {
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = getClass().getResourceAsStream("/perforce.xml");
                    out = new FileOutputStream(dest);
                    byte[] buf = new byte[1024];
                    int read;
                    while ((read = in.read(buf, 0, buf.length)) >= 0) {
                        out.write(buf, 0, read);
                    }
                } finally {
                    if (in != null) try {
                        in.close();
                    } catch (Exception e) {
                    }
                    if (out != null) try {
                        out.close();
                    } catch (Exception e) {
                    }
                }
            } catch (IOException e) {
                Log.log(Log.WARNING, this, "couldn't install mode file");
                Log.log(Log.WARNING, this, e);
                return;
            } catch (Exception e) {
                Log.log(Log.ERROR, this, e);
                return;
            }
            Mode mode = jEdit.getMode("perforce");
            if (mode == null) {
                mode = new Mode("perforce");
                mode.setProperty("file", dest);
                mode.unsetProperty("filenameGlob");
                mode.setProperty("firstlineGlob", "# A Perforce {Branch,Client,Change,User} Specification.*");
                mode.init();
                jEdit.addMode(mode);
            }
            jEdit.setIntegerProperty("p4plugin.mode_file_version", MODE_FILE_VERSION);
        }
    }
