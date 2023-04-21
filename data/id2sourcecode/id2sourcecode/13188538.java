    public InputStream getInputStream() {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            Assertions.UNREACHABLE();
            return null;
        }
    }
