    private void postChunk(MessageEvent e) {
        HttpChunk chunk = (HttpChunk) e.getMessage();
        try {
            decoder.offer(chunk);
        } catch (ErrorDataDecoderException e1) {
            status = HttpResponseStatus.NOT_ACCEPTABLE;
            sendError(e.getChannel(), "While decoding chunk: " + e1.getMessage());
            return;
        }
        readHttpDataChunkByChunk(e.getChannel());
        if (chunk.isLast()) {
            storeFile(e.getChannel());
            readingChunks = false;
        }
    }
