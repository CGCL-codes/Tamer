    public static Reader resourceReader(Class owner_class, String file_name) {
        Reader res = null;
        try {
            URL url = owner_class.getResource(file_name);
            res = new InputStreamReader(url.openStream());
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        return res;
    }
