        public void contentAvailable(ContentDecoder decoder, IOControl ioctrl) throws IOException {
            long transferred;
            if (useFileChannels && decoder instanceof FileContentDecoder) {
                transferred = ((FileContentDecoder) decoder).transfer(fileChannel, idx, Long.MAX_VALUE);
            } else {
                transferred = fileChannel.transferFrom(new ContentDecoderChannel(decoder), idx, Long.MAX_VALUE);
            }
            if (transferred > 0) idx += transferred;
        }
