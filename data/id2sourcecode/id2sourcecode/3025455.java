    private void createTempFilesForProWs() {
        try {
            lib = new File(System.getProperty("java.io.tmpdir"), "lib");
            if (!lib.exists()) lib.mkdirs();
            File modules = new File(lib.getAbsolutePath() + File.separator + "modules");
            if (!modules.exists()) modules.mkdirs();
            File rahas = new File(System.getProperty("java.io.tmpdir"), "lib" + File.separator + "modules" + File.separator + "rahas-1.5.mar");
            if (!rahas.exists()) {
                rahas.createNewFile();
                FileOutputStream fos = new FileOutputStream(rahas);
                InputStream is = PlasmoQuery.class.getResourceAsStream("/lib/modules/rahas-1.5.mar");
                byte[] bytes = new byte[4096];
                int read = 0;
                while ((read = is.read(bytes)) != -1) {
                    fos.write(bytes, 0, read);
                }
                is.close();
                fos.close();
            }
            File rampart = new File(System.getProperty("java.io.tmpdir"), "lib" + File.separator + "modules" + File.separator + "rampart-1.5.mar");
            if (!rampart.exists()) {
                rampart.createNewFile();
                FileOutputStream fos = new FileOutputStream(rampart);
                InputStream is = PlasmoQuery.class.getResourceAsStream("/lib/modules/rampart-1.5.mar");
                byte[] bytes = new byte[4096];
                int read = 0;
                while ((read = is.read(bytes)) != -1) {
                    fos.write(bytes, 0, read);
                }
                is.close();
                fos.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create temp files for loading of security modules. File system may be corrupt.");
        }
    }
