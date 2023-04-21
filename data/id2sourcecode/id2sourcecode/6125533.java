    private void executeScriptToGetResponseLines() throws IOException {
        logger.debug("Ready to execute the script " + this.getRequestedResource().getPath());
        FileChannel channel = new FileInputStream(this.getRequestedFile()).getChannel();
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, this.getRequestedFile().length());
        Charset charset = Charset.forName("ISO-8859-1");
        CharsetDecoder decoder = charset.newDecoder();
        List<String> lines = new ArrayList<String>();
        StringBuilder builder = new StringBuilder();
        if (this.wasErrorOnRequestBeforeScriptExecution()) {
            logger.debug("Handler error: executing the script with " + this.getClass().getSimpleName());
            channel = new FileInputStream(this.getRequestedFile()).getChannel();
            buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, this.getRequestedFile().length());
            CharBuffer charBuffer = decoder.decode(buffer);
            for (int i = 0, n = charBuffer.length(); i < n; i++) {
                char charValue = charBuffer.get();
                if (charValue != '\n') {
                    builder.append(charValue);
                } else {
                    lines.add(builder.toString().replace("$SERVER_ADMIN", VOctopusConfigurationManager.WebServerProperties.HTTPD_CONF.getPropertyValue("ServerAdmin")).replace("$REQUESTED_RESOURCE", this.getRequestedResource().getPath()));
                    builder.delete(0, builder.capacity());
                }
            }
            this.scriptsLines = lines.toArray(new String[lines.size()]);
        } else if (getScriptPathForAlias(this.request.getUri()) != null) {
            String[] args = null;
            if (this.requestParameters != null) {
                args = new String[this.requestParameters.size()];
                int i = -1;
                for (String arg : this.requestParameters.keySet()) {
                    args[++i] = arg + "=" + this.requestParameters.get(arg);
                }
            }
            try {
                this.scriptsLines = this.getCgiExecutionResponse(args);
                if (this.scriptsLines[0].toLowerCase().contains("content-type:")) {
                    this.contentType = this.scriptsLines[0].split(" ")[1].trim();
                } else {
                    this.contentType = "text/plain";
                }
                this.requestType = RequestType.ASCII;
                this.status = ReasonPhase.STATUS_200;
            } catch (CgiExecutionException e) {
                this.status = ReasonPhase.STATUS_500;
                this.contentType = "text/html";
                this.requestType = RequestType.ASCII;
                File error500 = VOctopusConfigurationManager.get500ErrorFile();
                System.out.println("Request generated a 500: handler chose " + error500.getPath());
                channel = new FileInputStream(error500).getChannel();
                buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, error500.length());
                CharBuffer charBuffer = decoder.decode(buffer);
                for (int i = 0, n = charBuffer.length(); i < n; i++) {
                    char charValue = charBuffer.get();
                    if (charValue != '\n') {
                        builder.append(charValue);
                    } else {
                        String complete = builder.toString();
                        String changed = complete.replace("$REQUESTED_RESOURCE", this.getRequestedResource().getPath()).replace("$REASON", e.getMessage());
                        lines.add(changed);
                        builder.delete(0, builder.capacity());
                    }
                }
                this.scriptsLines = lines.toArray(new String[lines.size()]);
            }
        }
    }
