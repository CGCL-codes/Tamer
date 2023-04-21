    public DeskletConfig installDesklet(String uuid, URL installFrom) throws MalformedURLException, IOException, JDOMException {
        SecurityManager sm = System.getSecurityManager();
        Object ctx = sm.getSecurityContext();
        sm.checkPermission(PERMISSION, ctx);
        URL url = installFrom;
        if (url.getProtocol().equals(PROTOCOL)) {
            String ext = installFrom.toExternalForm();
            url = new URL("http" + ext.substring(PROTOCOL.length(), ext.length()));
        }
        File file = new File(Environment.HOME, uuid + ".jar");
        core.getMainPanel().getSpinner().setBusy(true);
        try {
            String[] parts = url.getFile().split("/");
            if (parts.length > 0) {
                String deskletName = parts[parts.length - 1];
                core.getMainPanel().getSpinner().setText(deskletName);
            }
            StreamUtility.copyStream(url.openStream(), new FileOutputStream(file));
        } catch (IOException ex) {
            core.getMainPanel().getSpinner().setText("error");
            core.getMainPanel().getSpinner().setBusy(false);
            throw ex;
        }
        core.getMainPanel().getSpinner().setText("");
        core.getMainPanel().getSpinner().setBusy(false);
        JarFile jar = new JarFile(file);
        File destination = new File(Environment.HOME, uuid);
        destination.mkdirs();
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            LOG.log(Level.FINE, "Writing: " + entry.getName());
            if (entry.isDirectory()) {
                new File(destination, entry.getName()).mkdirs();
            } else {
                File entryFile = new File(destination, entry.getName());
                entryFile.getParentFile().mkdirs();
                StreamUtility.copyStream(jar.getInputStream(entry), new FileOutputStream(entryFile));
            }
        }
        DeskletConfig cfg = this.readDeskletConfig(destination);
        return cfg;
    }
