    private void post(MessageEvent e) {
        getUriArgs();
        try {
            decoder = new HttpPostRequestDecoder(factory, request);
        } catch (ErrorDataDecoderException e1) {
            status = HttpResponseStatus.NOT_ACCEPTABLE;
            sendError(e.getChannel(), "While decoder creation: " + e1.getMessage());
            return;
        } catch (IncompatibleDataDecoderException e1) {
            status = HttpResponseStatus.NOT_ACCEPTABLE;
            sendError(e.getChannel(), "While decoder creation: " + e1.getMessage());
            return;
        }
        if (request.isChunked()) {
            readingChunks = true;
        } else {
            readHttpDataAllReceive(e.getChannel());
            storeFile(e.getChannel());
        }
    }
