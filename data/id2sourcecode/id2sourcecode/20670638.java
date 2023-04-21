    public boolean matches(Object item) {
        if (item == null) return false;
        URL url;
        if (item instanceof URL) {
            url = (URL) item;
        } else if (item instanceof String) {
            String urlAsText = (String) item;
            try {
                url = new URL(urlAsText);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(urlAsText, e);
            }
        } else {
            throw new IllegalArgumentException("illegal item type: " + item.getClass().getName());
        }
        try {
            URLConnection conn = url.openConnection();
            conn.getInputStream();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
