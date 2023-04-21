    protected void handleChannelType(Observable obs, Object ov) {
        AsteriskChanTypeTablePdu prev;
        int errStatus = _atPdu.getErrorStatus();
        int errIndex = _atPdu.getErrorIndex();
        if (errStatus == AsnObject.SNMP_ERR_NOERROR) {
            prev = _atPdu;
            String name = _atPdu.getAstChanTypeName();
            Node cTypeNode = _graph.addNode();
            cTypeNode.setString(CTYPE, name);
            cTypeNode.setBoolean(ISACT, false);
            _channelTypeMap.put(name, new Integer(cTypeNode.getRow()));
            getChannelTypes(_context, prev);
        } else {
            _viz.run(FONT);
            _viz.run(LAYOUT);
            _viz.run(COLOUR);
            _viz.run(REPAINT);
            startThread();
        }
    }
