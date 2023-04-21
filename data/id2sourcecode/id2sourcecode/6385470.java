    public Channel setUpChannel(String type, ParticipantConnectionImpl connection, String docId) throws ConnectionException {
        LOG.debug("--> setUpChannel(type=" + type + ")");
        Channel channel = startChannel(type, connection);
        try {
            String channelType = getChannelTypeXML(type, docId);
            byte[] data = channelType.getBytes(NetworkProperties.get(NetworkProperties.KEY_DEFAULT_ENCODING));
            OutputDataStream output = DataStreamHelper.prepare(data);
            LOG.debug("--> sendMSG() for channel type");
            Reply reply = new Reply();
            channel.sendMSG(output, reply);
            if (type.equals(CHANNEL_DISCOVERY)) {
                InputDataStream response = reply.getNextReply().getDataStream();
                byte[] rawData = DataStreamHelper.read(response);
                ResponseParserHandler handler = new ResponseParserHandler();
                DeserializerImpl.getInstance().deserialize(rawData, handler);
                Request result = handler.getResult();
                if (result.getType() == ProtocolConstants.USER_DISCOVERY) {
                    getUser().setId(result.getUserId());
                    String name = (String) result.getPayload();
                    getUser().getMutableUserDetails().setUsername(name);
                } else {
                    LOG.error("unknown response type parsed.");
                }
            } else {
                reply.getNextReply();
            }
            LOG.debug("<-- sendMSG()");
            LOG.debug("<-- setUpChannel()");
            return channel;
        } catch (Exception be) {
            LOG.error("could not start channel [" + be + "]");
            throw new ConnectionException("could not start channel");
        }
    }
