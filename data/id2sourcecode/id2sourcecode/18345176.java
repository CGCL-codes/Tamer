    private void writeKetuXpi(HttpServletResponse response) throws IOException {
        response.setContentType("application/x-xpinstall");
        response.setContentLength(33466);
        OutputStream ostream = response.getOutputStream();
        try {
            ZipOutputStream zip = new ZipOutputStream(ostream);
            zip.setLevel(9);
            ZipEntry entry = new ZipEntry("install.js");
            zip.putNextEntry(entry);
            copyToOutputStream(zip, "WEB-INF/install.js");
            entry = new ZipEntry("libketuplugin.so");
            zip.putNextEntry(entry);
            extractToOutputStream(zip, "WEB-INF/lib/ketu-plugin-0.2-SNAPSHOT-native-i386-linux.jar", "lib/i386/linux/libketuplugin.so");
            zip.close();
        } finally {
            ostream.flush();
        }
    }
