    public void produceContent(ContentEncoder encoder, IOControl ioctrl) throws IOException {
        if (fileChannel == null) {
            FileInputStream in = new FileInputStream(file);
            fileChannel = in.getChannel();
            idx = 0;
        }
        long transferred;
        if (useFileChannels && encoder instanceof FileContentEncoder) {
            transferred = ((FileContentEncoder) encoder).transfer(fileChannel, idx, Long.MAX_VALUE);
        } else {
            transferred = fileChannel.transferTo(idx, Long.MAX_VALUE, new ContentEncoderChannel(encoder));
        }
        if (transferred > 0) idx += transferred;
        if (idx >= fileChannel.size()) encoder.complete();
    }
