    @Override
    public void addContent(ChannelBuffer buffer, boolean last) throws IOException {
        if (fileUpload instanceof MemoryFileUpload) {
            if (fileUpload.length() + buffer.readableBytes() > limitSize) {
                DiskFileUpload diskFileUpload = new DiskFileUpload(fileUpload.getName(), fileUpload.getFilename(), fileUpload.getContentType(), fileUpload.getContentTransferEncoding(), fileUpload.getCharset(), definedSize);
                if (((MemoryFileUpload) fileUpload).getChannelBuffer() != null) {
                    diskFileUpload.addContent(((MemoryFileUpload) fileUpload).getChannelBuffer(), false);
                }
                fileUpload = diskFileUpload;
            }
        }
        fileUpload.addContent(buffer, last);
    }
