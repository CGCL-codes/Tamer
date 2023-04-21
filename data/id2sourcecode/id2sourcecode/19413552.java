    private JarFile getJar(String target, boolean loader) {
        while (true) {
            try {
                String s = "jar:http://world" + world + "." + target + ".com/";
                if (loader) {
                    s += "loader.jar!/";
                } else {
                    s += target + ".jar!/";
                }
                URL url = new URL(s);
                JarURLConnection juc = (JarURLConnection) url.openConnection();
                juc.setConnectTimeout(5000);
                return juc.getJarFile();
            } catch (Exception ignored) {
                world = nextWorld();
            }
        }
    }
