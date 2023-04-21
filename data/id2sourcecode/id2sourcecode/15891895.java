    public static InputStream getInputStream(String uri) throws IOException {
        InputStream is = null;
        URL url = null;
        File file = null;
        switch(PathUtil.getProtocol(uri)) {
            case PathUtil.SYSTEM:
                file = new File(uri);
                if (file.exists()) {
                    try {
                        is = new FileInputStream(file);
                    } catch (FileNotFoundException ex) {
                        is = null;
                        throw new IOException(ex.getMessage());
                    }
                } else {
                    is = null;
                    throw new IOException("File " + file.getPath() + " does not exist");
                }
                break;
            case PathUtil.URL_FILE:
                file = new File(PathUtil.URIToPath(uri));
                if (file.exists()) {
                    try {
                        file = new File(PathUtil.URIToPath(uri));
                        is = new FileInputStream(file);
                    } catch (FileNotFoundException ex) {
                        is = null;
                        throw new IOException(ex.getMessage());
                    }
                } else {
                    is = null;
                    throw new IOException("File " + file.getPath() + " does not exist");
                }
                break;
            case PathUtil.URL_FTP:
                try {
                    url = new URL(uri);
                    is = new BufferedInputStream(url.openStream());
                } catch (MalformedURLException ex) {
                    is = null;
                    throw new IOException(ex.getMessage());
                } catch (IOException ex) {
                    is = null;
                    throw new IOException(ex.getMessage());
                }
                break;
            case PathUtil.URL_HTTP:
                try {
                    url = new URL(uri);
                    is = url.openStream();
                } catch (MalformedURLException ex) {
                    is = null;
                    throw new IOException(ex.getMessage());
                } catch (IOException ex) {
                    is = null;
                    throw new IOException(ex.getMessage());
                }
                break;
            default:
                is = null;
                throw new IOException("Cannot determine protocol used by " + uri);
        }
        return is;
    }
