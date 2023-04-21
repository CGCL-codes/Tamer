        public void run() {
            AgiChannel channel = null;
            try {
                AgiReader reader;
                AgiWriter writer;
                AgiRequest request;
                AgiScript script;
                reader = createReader();
                writer = createWriter();
                request = reader.readRequest();
                channel = new AgiChannelMinaImpl(request, writer, reader);
                script = mappingStrategy.determineScript(request);
                if (script == null) {
                    final String errorMessage;
                    errorMessage = "No script configured for URL '" + request.getRequestURL() + "' (script '" + request.getScript() + "')";
                    logger.error(errorMessage);
                    setStatusVariable(channel, AJ_AGISTATUS_NOT_FOUND);
                    logToAsterisk(channel, errorMessage);
                } else {
                    runScript(script, request, channel);
                }
            } catch (AgiException e) {
                setStatusVariable(channel, AJ_AGISTATUS_FAILED);
                logger.error("AgiException while handling request", e);
            } catch (Exception e) {
                setStatusVariable(channel, AJ_AGISTATUS_FAILED);
                logger.error("Unexpected Exception while handling request", e);
            } finally {
                try {
                    this.in.close();
                    this.out.close();
                    this.session.close();
                } catch (IOException e) {
                }
            }
        }
