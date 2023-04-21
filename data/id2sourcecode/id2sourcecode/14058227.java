    public void addContent(ChannelBuffer buffer, boolean last) throws IOException {
        if (buffer != null) {
            int localsize = buffer.readableBytes();
            if (definedSize > 0 && definedSize < size + localsize) {
                throw new IOException("Out of size: " + (size + localsize) + " > " + definedSize);
            }
            ByteBuffer byteBuffer = buffer.toByteBuffer();
            int written = 0;
            if (file == null) {
                file = tempFile();
            }
            if (fileChannel == null) {
                FileOutputStream outputStream = new FileOutputStream(file);
                fileChannel = outputStream.getChannel();
            }
            while (written < localsize) {
                written += fileChannel.write(byteBuffer);
            }
            size += localsize;
            buffer.readerIndex(buffer.readerIndex() + written);
        }
        if (last) {
            if (file == null) {
                file = tempFile();
            }
            if (fileChannel == null) {
                FileOutputStream outputStream = new FileOutputStream(file);
                fileChannel = outputStream.getChannel();
            }
            fileChannel.force(false);
            fileChannel.close();
            fileChannel = null;
            completed = true;
        } else {
            if (buffer == null) {
                throw new NullPointerException("buffer");
            }
        }
    }
