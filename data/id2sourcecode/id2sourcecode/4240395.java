    public void process() {
        JSONReader reader = new JSONReader();
        @SuppressWarnings("unchecked") Map<String, Object> doc = (Map<String, Object>) reader.read(WSJSONWriter.writeMetadata(getMetadataModelMap(this.codec.getEndpoint(), true), this.codec.getCustomSerializer()));
        StringBuffer buffer = new StringBuffer();
        getJQTree(doc, buffer, 0);
        endPointDocuments.put(this.codec.getEndpoint().getServiceName(), buffer.toString());
    }
