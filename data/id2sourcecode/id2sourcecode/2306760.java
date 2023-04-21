    public InterproNameHandler(URL url) throws NameNotFoundException {
        try {
            InputStream stream = url.openStream();
            if (interproMap == null || interproMap.isEmpty()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                try {
                    init(reader);
                } catch (IOException e) {
                    throw new NameNotFoundException("Can not read " + stream.toString());
                }
            }
        } catch (IOException e) {
            throw new NameNotFoundException("Can not open stream for  " + url.getPath());
        }
    }
