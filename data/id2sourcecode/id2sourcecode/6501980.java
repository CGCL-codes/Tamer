    public DocumentFragmentInputSource(InputSource inputSource, String namespacePrefix, Map namespaces) throws IOException {
        if (inputSource.getByteStream() != null) {
            this.setByteStream(new AddRootElementInputStream(inputSource.getByteStream(), namespacePrefix, namespaces));
        } else if (inputSource.getSystemId() != null) {
            InputStream urlStream = new URL(inputSource.getSystemId()).openStream();
            this.setByteStream(new AddRootElementInputStream(urlStream, namespacePrefix, namespaces));
            this.setSystemId(inputSource.getSystemId());
        } else {
            this.setCharacterStream(new AddRootElementReader(inputSource.getCharacterStream(), namespacePrefix, namespaces));
        }
    }
