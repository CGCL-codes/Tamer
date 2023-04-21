    public void ref(URL url) {
        try {
            setUrl(url);
            ref(url.openStream());
        } catch (Exception e) {
        }
    }
