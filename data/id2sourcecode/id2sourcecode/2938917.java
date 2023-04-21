        private void handleRequest(StartLine inLine, Header inHeader, MessageReader reader) throws IOException {
            registerAcks(inHeader.getField("RMI-Response-Ack"));
            boolean persist = supportsPersist(inLine, inHeader);
            boolean chunk = supportsChunking(inLine, inHeader);
            MessageWriter writer = new MessageWriter(out, chunk);
            writer.writeStartLine(new StartLine(HTTP_MAJOR, HTTP_MINOR, HttpURLConnection.HTTP_OK, "OK"));
            writer.writeHeader(createResponseHeader(persist));
            InboundRequestImpl req = new InboundRequestImpl(reader, writer);
            try {
                dispatcher.dispatch(req);
            } catch (Throwable th) {
            }
            req.finish();
            if (!persist || req.streamCorrupt()) {
                shutdown(true);
            }
        }
