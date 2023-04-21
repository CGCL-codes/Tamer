    private DataInputStream getCompatibleInput(TranscoderInput input) throws TranscoderException {
        if (input == null) {
            handler.fatalError(new TranscoderException(String.valueOf(ERROR_NULL_INPUT)));
        }
        InputStream in = input.getInputStream();
        if (in != null) {
            return new DataInputStream(new BufferedInputStream(in));
        }
        String uri = input.getURI();
        if (uri != null) {
            try {
                URL url = new URL(uri);
                in = url.openStream();
                return new DataInputStream(new BufferedInputStream(in));
            } catch (MalformedURLException e) {
                handler.fatalError(new TranscoderException(e));
            } catch (IOException e) {
                handler.fatalError(new TranscoderException(e));
            }
        }
        handler.fatalError(new TranscoderException(String.valueOf(ERROR_INCOMPATIBLE_INPUT_TYPE)));
        return null;
    }
