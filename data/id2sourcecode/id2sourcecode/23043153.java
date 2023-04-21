    public Boolean canRead() {
        if (urlFormat) {
            try {
                URL url = getUrl();
                if (url == null) return null;
                url.openStream().close();
                return Boolean.TRUE;
            } catch (MalformedURLException e) {
                return Boolean.FALSE;
            } catch (IOException e) {
                return Boolean.FALSE;
            }
        } else {
            File file = getFile();
            if (file == null) return null;
            return file.exists() && file.canRead();
        }
    }
