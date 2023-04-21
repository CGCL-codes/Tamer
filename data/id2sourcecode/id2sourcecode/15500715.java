    private org.omg.CORBA.portable.OutputStream _invoke_connect_push_consumer(final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        org.omg.CosEventComm.PushConsumer arg0_in = org.omg.DsObservationAccess.PushConsumerHelper.read(_is);
        try {
            connect_push_consumer(arg0_in);
            _output = handler.createReply();
        } catch (org.omg.CosEventChannelAdmin.AlreadyConnected _exception) {
            _output = handler.createExceptionReply();
            org.omg.CosEventChannelAdmin.AlreadyConnectedHelper.write(_output, _exception);
        }
        return _output;
    }
