    public String getChannelId() {
        Message message = getRequest();
        if (message != null) {
            Object id = message.getHeader(Message.ENDPOINT_HEADER);
            if (id instanceof String) return (String) id;
        }
        return null;
    }
