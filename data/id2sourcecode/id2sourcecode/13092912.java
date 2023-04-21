    @Override
    public void addContent(ChannelBuffer buffer, boolean last) throws IOException {
        if (attribute instanceof MemoryAttribute) {
            if (attribute.length() + buffer.readableBytes() > limitSize) {
                DiskAttribute diskAttribute = new DiskAttribute(attribute.getName());
                if (((MemoryAttribute) attribute).getChannelBuffer() != null) {
                    diskAttribute.addContent(((MemoryAttribute) attribute).getChannelBuffer(), false);
                }
                attribute = diskAttribute;
            }
        }
        attribute.addContent(buffer, last);
    }
