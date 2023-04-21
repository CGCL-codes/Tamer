    public XMLOutputStreamWriter addAttribute(String attributeName, String attributeValue) throws IOException {
        if (this.isCurrentElementReady()) {
            if (this.isCurrentAcceptAttribute()) {
                this.writer.write(' ');
                this.writer.write(attributeName);
                this.writer.write("=\"");
                this.writeAttributeValue(attributeValue.toCharArray());
                this.writer.write('"');
            } else {
                throw new IllegalStateException("Cannot add attribute because content have " + "aready been writed for current element");
            }
        } else {
            throw new IllegalStateException("Cannot add attribute there is no current element ready");
        }
        return this;
    }
