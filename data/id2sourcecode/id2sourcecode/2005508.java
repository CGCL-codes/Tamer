    protected TranscoderInput createTranscoderInput() {
        try {
            URL url = resolveURL(inputURI);
            InputStream istream = url.openStream();
            TranscoderInput input = new TranscoderInput(istream);
            input.setURI(url.toString());
            return input;
        } catch (IOException ex) {
            throw new IllegalArgumentException(inputURI);
        }
    }
