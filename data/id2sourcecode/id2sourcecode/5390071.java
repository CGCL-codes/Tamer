    public static String getInfo() throws IOException {
        Properties props = new Properties();
        URL urlFichierProp = new File("db4oconfig.txt").toURI().toURL();
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(urlFichierProp.openStream());
            props.load(bis);
            return props.getProperty("base");
        } finally {
            if (bis != null) {
                bis.close();
            }
        }
    }
