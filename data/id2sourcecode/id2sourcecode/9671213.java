    static final boolean load(String lib, String jarpath) {
        try {
            Runtime.getRuntime().loadLibrary(lib);
            return true;
        } catch (Throwable ex) {
        }
        try {
            URL u = LibHelper.class.getProtectionDomain().getCodeSource().getLocation();
            InputStream is = u.openStream();
            JarInputStream jar = new JarInputStream(is);
            JarEntry next = null;
            while ((next = jar.getNextJarEntry()) != null) {
                if (next.getName().equals(jarpath)) {
                    break;
                }
            }
            if (next == null) {
                return false;
            }
            File tmp = File.createTempFile("jaffer-", "-lib");
            tmp.deleteOnExit();
            FileOutputStream os = new FileOutputStream(tmp);
            byte b[] = new byte[4096];
            int read = 0;
            while ((read = jar.read(b)) > 0) {
                os.write(b, 0, read);
            }
            os.close();
            if (tmp != null) {
                Runtime.getRuntime().load(tmp.getAbsolutePath());
            }
            return true;
        } catch (Throwable ex) {
            ex.printStackTrace();
            return false;
        }
    }
