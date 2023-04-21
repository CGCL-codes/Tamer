    public org.omg.CORBA.portable.OutputStream _invoke(String opName, org.omg.CORBA.portable.InputStream _is, org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output = null;
        if (opName.equals("connect_push_consumer")) {
            org.omg.CosEventComm.PushConsumer arg0_in = org.omg.CosEventComm.PushConsumerHelper.read(_is);
            try {
                connect_push_consumer(arg0_in);
                _output = handler.createReply();
            } catch (org.omg.CosEventChannelAdmin.AlreadyConnected _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosEventChannelAdmin.AlreadyConnectedHelper.write(_output, _exception);
            } catch (org.omg.CosEventChannelAdmin.TypeError _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosEventChannelAdmin.TypeErrorHelper.write(_output, _exception);
            }
            return _output;
        } else if (opName.equals("disconnect_push_supplier")) {
            disconnect_push_supplier();
            _output = handler.createReply();
            return _output;
        } else throw new org.omg.CORBA.BAD_OPERATION();
    }
