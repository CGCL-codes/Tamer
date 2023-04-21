    public Integer addChannel(Channel channel) throws JCouplingException {
        OracleConnection connection = null;
        OracleCallableStatement callableStatement = null;
        String callString = null;
        Integer channelid = null;
        try {
            connection = (OracleConnection) ConnectionManager.getConnection();
            callString = new String("{? = call pkg_channel.ADD_(? , ? , ? )}");
            callableStatement = (OracleCallableStatement) connection.prepareCall(callString);
            callableStatement.registerOutParameter(1, OracleTypes.NUMBER);
            callableStatement.setString(2, channel.getChannelName());
            callableStatement.setInt(3, channel.getMiddlewareAdapterID());
            String wsdlInfo = channel.getWsdlChannelUrl() + "," + channel.getWsdlChannelPortType() + "," + channel.getWsdlChannelOperationName();
            callableStatement.setString(4, wsdlInfo);
            logger.debug(logCallableStatement(callString, new Object[] { channel.getChannelName(), channel.getMiddlewareAdapterID(), wsdlInfo }));
            callableStatement.executeQuery();
            channelid = new Integer(callableStatement.getInt(1));
        } catch (SQLException sqlex) {
            throw new JCouplingException("cannot store channel", sqlex);
        } finally {
            try {
                if (callableStatement != null) callableStatement.close();
            } catch (SQLException e) {
                logger.warn("cannot close prepared statement", e);
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                logger.warn("cannot close connection", e);
            }
        }
        return channelid;
    }
