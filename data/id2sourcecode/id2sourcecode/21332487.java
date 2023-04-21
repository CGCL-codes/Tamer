    public void run() {
        AgiChannel channel = null;
        try {
            AgiReader reader;
            AgiWriter writer;
            AgiRequest request;
            reader = createReader();
            writer = createWriter();
            request = reader.readRequest();
            channel = new AgiChannelImpl(request, writer, reader);
            AgiConnectionHandler.channel.set(channel);
            if (mappingStrategy != null) {
                script = mappingStrategy.determineScript(request);
            }
            if (script == null && !ignoreMissingScripts) {
                final String errorMessage;
                errorMessage = "No script configured for URL '" + request.getRequestURL() + "' (script '" + request.getScript() + "')";
                logger.error(errorMessage);
                setStatusVariable(channel, AJ_AGISTATUS_NOT_FOUND);
                logToAsterisk(channel, errorMessage);
            } else if (script != null) {
                runScript(script, request, channel);
            }
        } catch (AgiException e) {
            setStatusVariable(channel, AJ_AGISTATUS_FAILED);
            logger.error("AgiException while handling request", e);
        } catch (Exception e) {
            setStatusVariable(channel, AJ_AGISTATUS_FAILED);
            logger.error("Unexpected Exception while handling request", e);
        } finally {
            AgiConnectionHandler.channel.set(null);
            release();
        }
    }
