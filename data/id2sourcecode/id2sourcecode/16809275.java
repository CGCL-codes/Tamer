    public static String search(Map params) {
        String keyword = (String) params.get(KEYWORD);
        String urlFull = URL + "&keywords=" + keyword;
        InputStream input = null;
        try {
            input = new URL(urlFull).openStream();
        } finally {
            if (input != null) input.close();
        }
        return null;
    }
