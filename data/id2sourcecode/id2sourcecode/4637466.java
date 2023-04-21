    public static BufferedReader abrirStream(URL url) {
        try {
            return new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return abrirStream(url);
        }
    }
