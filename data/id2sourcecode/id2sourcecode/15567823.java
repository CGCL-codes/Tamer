    public String[] getResourceLines() throws IOException {
        if (this.requestedResourceExists()) {
            logger.debug("Serving the file " + this.getRequestedFile());
            FileChannel channel = new FileInputStream(this.getRequestedFile()).getChannel();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, this.getRequestedFile().length());
            Charset charset = Charset.forName("ISO-8859-1");
            CharsetDecoder decoder = charset.newDecoder();
            CharBuffer charBuffer = decoder.decode(buffer);
            List<String> lines = new ArrayList<String>();
            StringBuilder builder = new StringBuilder();
            if (charBuffer.length() == 0) {
                this.setStatus(ReasonPhase.STATUS_204);
                return new String[] { "" };
            }
            for (int i = 0, n = charBuffer.length(); i < n; i++) {
                char charValue = charBuffer.get();
                if (charValue != '\n') {
                    builder.append(charValue);
                } else {
                    lines.add(builder.toString());
                    builder.delete(0, builder.capacity());
                }
            }
            return lines.toArray(new String[lines.size()]);
        } else {
            return new String[] { "" };
        }
    }
