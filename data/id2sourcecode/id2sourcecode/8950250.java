    protected TranscoderInput createTranscoderInput() {
        try {
            URL url = resolveURL(inputURI);
            Reader reader = new InputStreamReader(url.openStream());
            TranscoderInput input = new TranscoderInput(reader);
            input.setURI(url.toString());
            return input;
        } catch (IOException ex) {
            throw new IllegalArgumentException(inputURI);
        }
    }
