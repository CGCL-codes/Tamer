    public InputStream getFile(String filename, String location) throws SourceException {
        String res_file;
        if (location.equals("_top_")) {
            res_file = filename;
        } else {
            res_file = location + "/" + filename;
        }
        InputStream result = null;
        if (getClass().getClassLoader() != null) {
            result = getClass().getClassLoader().getResourceAsStream(res_file);
        } else {
            result = ClassLoader.getSystemResourceAsStream(res_file);
        }
        if (result != null) return result;
        if (baseURL != null) {
            URL resURL = null;
            try {
                resURL = new URL(baseURL.getProtocol(), baseURL.getHost(), baseURL.getFile() + "/" + res_file);
            } catch (MalformedURLException e) {
                System.err.println("MalformedURL " + resURL + " : " + e);
                e.printStackTrace();
                throw new SourceException(e.toString());
            }
            URLConnection urlCon = null;
            try {
                urlCon = resURL.openConnection();
                urlCon.connect();
                return urlCon.getInputStream();
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    throw new SourceHostNotFoundException(baseURL.getHost());
                }
                if (e instanceof FileNotFoundException) {
                    throw new SourceFileNotFoundException(resURL.toExternalForm());
                }
                System.err.println("can not open connection : " + e);
                throw new SourceException(e.toString());
            }
        }
        System.err.println("can not get file " + res_file);
        throw new SourceFileNotFoundException(res_file);
    }
