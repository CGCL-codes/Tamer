    public static ContentMetadataConstructor getConstructor() {
        return new ContentMetadataConstructor() {

            public ContentMetadata newInstance(Element metadataEl) throws IllegalArgumentException {
                return new Description(metadataEl);
            }
        };
    }
