    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        if (!readingChunks) {
            if (decoder != null) {
                decoder.cleanFiles();
                decoder = null;
            }
            HttpRequest request = this.request = (HttpRequest) e.getMessage();
            URI uri = null;
            try {
                uri = new URI(request.getUri());
            } catch (URISyntaxException e2) {
            }
            if (!uri.getPath().startsWith("/form")) {
                writeMenu(e);
                return;
            }
            responseContent.setLength(0);
            responseContent.append("WELCOME TO THE WILD WILD WEB SERVER\r\n");
            responseContent.append("===================================\r\n");
            responseContent.append("VERSION: " + request.getProtocolVersion().getText() + "\r\n");
            responseContent.append("REQUEST_URI: " + request.getUri() + "\r\n\r\n");
            responseContent.append("\r\n\r\n");
            List<Entry<String, String>> headers = request.getHeaders();
            for (Entry<String, String> entry : headers) {
                responseContent.append("HEADER: " + entry.getKey() + "=" + entry.getValue() + "\r\n");
            }
            responseContent.append("\r\n\r\n");
            Set<Cookie> cookies;
            String value = request.getHeader(HttpHeaders.Names.COOKIE);
            if (value == null) {
                cookies = Collections.emptySet();
            } else {
                CookieDecoder decoder = new CookieDecoder();
                cookies = decoder.decode(value);
            }
            for (Cookie cookie : cookies) {
                responseContent.append("COOKIE: " + cookie.toString() + "\r\n");
            }
            responseContent.append("\r\n\r\n");
            QueryStringDecoder decoderQuery = new QueryStringDecoder(request.getUri());
            Map<String, List<String>> uriAttributes = decoderQuery.getParameters();
            for (String key : uriAttributes.keySet()) {
                for (String valuen : uriAttributes.get(key)) {
                    responseContent.append("URI: " + key + "=" + valuen + "\r\n");
                }
            }
            responseContent.append("\r\n\r\n");
            try {
                decoder = new HttpPostRequestDecoder(factory, request);
            } catch (ErrorDataDecoderException e1) {
                e1.printStackTrace();
                responseContent.append(e1.getMessage());
                writeResponse(e.getChannel());
                Channels.close(e.getChannel());
                return;
            } catch (IncompatibleDataDecoderException e1) {
                responseContent.append(e1.getMessage());
                responseContent.append("\r\n\r\nEND OF GET CONTENT\r\n");
                writeResponse(e.getChannel());
                return;
            }
            responseContent.append("Is Chunked: " + request.isChunked() + "\r\n");
            responseContent.append("IsMultipart: " + decoder.isMultipart() + "\r\n");
            if (request.isChunked()) {
                responseContent.append("Chunks: ");
                readingChunks = true;
            } else {
                readHttpDataAllReceive(e.getChannel());
                responseContent.append("\r\n\r\nEND OF NOT CHUNKED CONTENT\r\n");
                writeResponse(e.getChannel());
            }
        } else {
            HttpChunk chunk = (HttpChunk) e.getMessage();
            try {
                decoder.offer(chunk);
            } catch (ErrorDataDecoderException e1) {
                e1.printStackTrace();
                responseContent.append(e1.getMessage());
                writeResponse(e.getChannel());
                Channels.close(e.getChannel());
                return;
            }
            responseContent.append('o');
            readHttpDataChunkByChunk(e.getChannel());
            if (chunk.isLast()) {
                readHttpDataAllReceive(e.getChannel());
                writeResponse(e.getChannel());
                readingChunks = false;
            }
        }
    }
