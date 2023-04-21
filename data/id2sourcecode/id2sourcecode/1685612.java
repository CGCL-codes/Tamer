    public static File toFile(URL url) {
        try {
            if (url.getProtocol().equals("jar") || url.getProtocol().equals("file")) {
                String path = url.getPath();
                if (path.startsWith("file://")) path = path.substring(7);
                path = URLDecoder.decode(path, "UTF-8");
                return new File(path);
            } else {
                File tmp = File.createTempFile("tmpZip", ".zip");
                IOUtil.transfer(url.openStream(), new FileOutputStream(tmp));
                return tmp;
            }
        } catch (IOException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
    }
