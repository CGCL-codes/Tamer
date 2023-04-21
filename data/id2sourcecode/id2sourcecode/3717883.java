    public static InputStream get(String urlString, String user, String pass, boolean robustMode) throws IOException {
        URLConnection uc = openConnection(urlString, user, pass, robustMode);
        if (uc == null) return null;
        return uc.getInputStream();
    }
