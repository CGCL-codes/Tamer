    @Override
    public iResource getResource(String ref) {
        try {
            if (root != null) {
                URL url = root.resolve(ref).toURL();
                url.openConnection();
                return new URLResource(url);
            } else {
                return new URLResource(new URL(ref));
            }
        } catch (IOException ex) {
            return null;
        }
    }
