    @SuppressWarnings("unchecked")
    public <T> T call(Object[] params) throws AmfFaultException {
        Message message = new Message();
        MessageBody body = new MessageBody();
        RemotingMessage remotingMessage = null;
        if (this.remoteMessage != null) {
            remotingMessage = this.remoteMessage;
        } else {
            remotingMessage = new RemotingMessage();
            if (this.destination == null) {
                throw new RuntimeException("not setting destination.");
            }
            if (this.operation == null) {
                throw new RuntimeException("not setting operation.");
            }
            remotingMessage.setHeader("version", AMF_VERSION_3);
            remotingMessage.setDestination(this.destination);
            remotingMessage.setOperation(this.operation);
            String messageId = UUID.randomUUID().toString();
            remotingMessage.setMessageId(messageId);
            List<Object> paramList = new ArrayList<Object>();
            for (Object param : params) {
                paramList.add(param);
            }
            remotingMessage.setBody(paramList);
        }
        body.setData(remotingMessage);
        body.setTarget("null");
        body.setResponse("/1");
        message.addBody(body);
        message.setVersion(3);
        Message responseMessage;
        try {
            URLConnection conn = this.urlObject.openConnection();
            logger.log(Level.INFO, "connect to [" + url + "] destination[" + this.destination + "] operation[" + this.operation + "]");
            conn.setRequestProperty(CONTENT_TYPE, CONTENT_TYPE_AMF);
            conn.setRequestProperty(CHARSET, UTF8);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            this.setHttpRequestCookieHeader(conn);
            OutputStream outputStream = conn.getOutputStream();
            AmfMessageProcessorImpl amfMessageProcessorImpl = createAmfMessageProcessorImpl();
            amfMessageProcessorImpl.writeRequestMessage(outputStream, message);
            InputStream inputStream = conn.getInputStream();
            this.processHttpResponseHeaders(conn.getHeaderFields());
            responseMessage = amfMessageProcessorImpl.readMessage(inputStream);
        } catch (Exception e) {
            throw new AmfFaultException(e);
        }
        MessageBody res = responseMessage.getBody(0);
        Object obj = res.getData();
        if (obj instanceof ErrorMessage) {
            ErrorMessage errMessage = (ErrorMessage) obj;
            final String code = errMessage.getFaultCode();
            final String msg = errMessage.getFaultDetail();
            throw new AmfFaultException(msg, code);
        } else if (obj instanceof AcknowledgeMessage) {
            AcknowledgeMessage ack = (AcknowledgeMessage) res.getData();
            Object response = ack.getBody();
            logger.log(Level.INFO, "response:" + response);
            return (T) response;
        } else {
            throw new RuntimeException("unknown MessageBody");
        }
    }
