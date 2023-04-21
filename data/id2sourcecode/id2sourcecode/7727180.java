    @Override
    public InputStream getResourceAsStream(String name) {
        InputStream in = _parent.getResourceAsStream(name);
        if (in == null) {
            URL url = findResource(name);
            if (url != null) {
                try {
                    in = url.openStream();
                } catch (IOException e) {
                    in = null;
                }
            }
        }
        return in;
    }
