    String getLocation(URL url) {
        try {
            String location = url.toString();
            if (location.startsWith("jar:file:/")) {
                File file = new File(url.getFile());
                return file.getPath().substring(6);
            } else if (location.startsWith("jar")) {
                url = ((JarURLConnection) url.openConnection()).getJarFileURL();
                return url.toString();
            } else if (location.startsWith("file")) {
                File file = new File(url.getFile());
                return file.getAbsolutePath();
            } else {
                return url.toString();
            }
        } catch (Throwable t) {
            return null;
        }
    }
