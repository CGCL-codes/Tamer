    private static void loadJarFiles() {
        JarInputStream jStream = null;
        try {
            System.out.println("here!");
            jStream = new JarInputStream(url.openStream());
            JarEntry entry = null;
            do {
                entry = jStream.getNextJarEntry();
                System.out.println(entry);
            } while (entry != null);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (jStream != null) {
                try {
                    jStream.close();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }
