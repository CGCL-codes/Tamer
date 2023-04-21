    public String prepareEmbeddedWebappToBeDeployable() throws IOException {
        args.remove("usage");
        args.remove("help");
        String kenshiraWebRoot = (String) args.remove("kenshira.webroot");
        System.getProperties().setProperty("kenshira.desktop", "true");
        if (kenshiraWebRoot != null) {
            args.put("webroot", kenshiraWebRoot);
            System.getProperties().setProperty("kenshira.webroot", kenshiraWebRoot);
        } else {
            String embeddedWarfileName = (String) args.get("war.embedded");
            if (embeddedWarfileName == null) embeddedWarfileName = "/kenshira.war";
            InputStream embeddedWarfile = KenshiraDesktop.class.getResourceAsStream(embeddedWarfileName);
            if (embeddedWarfile != null) {
                File tempWarfile = File.createTempFile("embedded", ".war").getAbsoluteFile();
                tempWarfile.getParentFile().mkdirs();
                tempWarfile.deleteOnExit();
                File tempWebroot = new File(tempWarfile.getParentFile(), "kenshiraRoot" + System.currentTimeMillis());
                tempWebroot.mkdirs();
                OutputStream out = new FileOutputStream(tempWarfile, true);
                int read = 0;
                byte buffer[] = new byte[BUFFER_BLOCK_SIZE];
                while ((read = embeddedWarfile.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                out.close();
                embeddedWarfile.close();
                args.put("warfile", tempWarfile.getAbsolutePath());
                args.put("webroot", tempWebroot.getAbsolutePath());
                args.remove("webappsDir");
                args.remove("hostsDir");
                kenshiraWebRoot = tempWebroot.getAbsolutePath();
                System.getProperties().setProperty("kenshira.warfile", tempWarfile.getAbsolutePath());
                System.getProperties().setProperty("kenshira.webroot", kenshiraWebRoot);
            } else {
                throw new RuntimeException(embeddedWarfileName + " no encontrado");
            }
        }
        return kenshiraWebRoot;
    }
