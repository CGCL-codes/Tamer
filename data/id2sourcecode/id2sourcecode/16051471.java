    public Channel getChannel() throws Exception {
        if (null != connection) {
            Channel chnl = connection.createChannel();
            chnl.queueDeclare(queueName, true, false, false, null);
            return chnl;
        } else {
            l.debug("connection is null");
            checkConnection();
            if (null != connection) {
                l.debug("retry create channel.");
                Channel chnl = connection.createChannel();
                chnl.queueDeclare(queueName, true, false, false, null);
                return chnl;
            }
        }
        return null;
    }
