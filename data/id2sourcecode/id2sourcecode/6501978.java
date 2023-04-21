    public DocumentFragmentInputSource(InputSource inputSource) throws IOException {
        if (inputSource.getByteStream() != null) {
            this.setByteStream(new AddRootElementInputStream(inputSource.getByteStream()));
        } else if (inputSource.getSystemId() != null) {
            InputStream urlStream = new URL(inputSource.getSystemId()).openStream();
            this.setByteStream(new AddRootElementInputStream(urlStream));
            this.setSystemId(inputSource.getSystemId());
        } else {
            this.setCharacterStream(new AddRootElementReader(inputSource.getCharacterStream()));
        }
    }
