    public void addContent(ChannelBuffer buffer, boolean last) throws IOException {
        if (fileUpload instanceof DefaultFileUpload) {
            if (fileUpload.length() + buffer.readableBytes() > limitSize) {
                DiskFileUpload diskFileUpload = new DiskFileUpload(fileUpload.getName(), fileUpload.getFilename(), fileUpload.getContentType(), fileUpload.getCharset(), definedSize);
                diskFileUpload.addContent(((DefaultFileUpload) fileUpload).getChannelBuffer(), false);
                fileUpload = diskFileUpload;
            }
        }
        fileUpload.addContent(buffer, last);
    }
