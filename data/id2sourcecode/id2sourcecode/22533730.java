    private boolean deployEmbeddedWarfile(final Map<String, String> args) {
        boolean result = Boolean.FALSE;
        final InputStream embeddedWarfile = BootStrap.class.getResourceAsStream(BootStrap.EMBEDDED_WAR);
        if (embeddedWarfile != null) {
            try {
                final File tempWarfile = File.createTempFile("embedded", ".war").getAbsoluteFile();
                tempWarfile.getParentFile().mkdirs();
                tempWarfile.deleteOnExit();
                final File tempWebroot = new File(tempWarfile.getParentFile(), BootStrap.WS_EMBEDDED_WAR);
                tempWebroot.mkdirs();
                BootStrap.logger.debug("Extracting embedded warfile to {}", tempWarfile.getAbsolutePath());
                final OutputStream out = new FileOutputStream(tempWarfile, Boolean.TRUE);
                int read = 0;
                final byte buffer[] = new byte[2048];
                while ((read = embeddedWarfile.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                out.close();
                embeddedWarfile.close();
                args.put("warfile", tempWarfile.getAbsolutePath());
                args.put("webroot", tempWebroot.getAbsolutePath());
                args.remove("webappsDir");
                args.remove("hostsDir");
                result = Boolean.TRUE;
            } catch (final IOException e) {
                BootStrap.logger.error("deployEmbeddedWarfile", e);
            }
        }
        return result;
    }
