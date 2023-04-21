    @Override
    protected InputStream getInputStream() {
        try {
            URLConnection uc = url.openConnection();
            return new BufferedInputStream(uc.getInputStream());
        } catch (IOException e) {
            return null;
        }
    }
