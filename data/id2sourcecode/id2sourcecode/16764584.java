    private File initHome(File homeDir) throws IOException {
        URL defaultWebXml = getClass().getResource("conf/web.xml");
        if (defaultWebXml == null) throw new IOException("Could not load default web.xml");
        File conf = new File(homeDir, "conf");
        if (!conf.isDirectory() && !conf.mkdirs()) throw new IOException("Could not create config directory " + conf.getAbsolutePath());
        FileOutputStream fos = new FileOutputStream(new File(conf, "web.xml"));
        try {
            InputStream ios = defaultWebXml.openStream();
            try {
                byte[] buf = new byte[512];
                for (int b = ios.read(buf); b >= 0; b = ios.read(buf)) if (b > 0) fos.write(buf, 0, b);
            } finally {
                ios.close();
            }
        } finally {
            fos.close();
        }
        return homeDir;
    }
