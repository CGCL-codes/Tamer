    protected String getLocation() {
        try {
            Class clazz = Loader.loadClass("org.apache.axis2.engine.AxisEngine");
            java.net.URL url = clazz.getProtectionDomain().getCodeSource().getLocation();
            String location = url.toString();
            if (location.startsWith("jar")) {
                url = ((java.net.JarURLConnection) url.openConnection()).getJarFileURL();
                location = url.toString();
            }
            if (location.startsWith("file")) {
                File file = Utils.toFile(url);
                return file.getAbsolutePath();
            } else {
                return url.toString();
            }
        } catch (Throwable t) {
            return null;
        }
    }
