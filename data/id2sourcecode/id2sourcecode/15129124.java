    public InputStream getInputStream() throws IOException {
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
        return uc.getInputStream();
    }
