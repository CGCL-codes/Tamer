    public org.omg.CORBA.portable.OutputStream _invoke(String opName, org.omg.CORBA.portable.InputStream _is, org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output = null;
        if (opName.equals("connect_push_supplier")) {
            org.omg.CosEventComm.PushSupplier arg0_in = org.omg.CosEventComm.PushSupplierHelper.read(_is);
            try {
                connect_push_supplier(arg0_in);
                _output = handler.createReply();
            } catch (org.omg.CosEventChannelAdmin.AlreadyConnected _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosEventChannelAdmin.AlreadyConnectedHelper.write(_output, _exception);
            }
            return _output;
        } else if (opName.equals("push")) {
            org.omg.CORBA.Any arg0_in = _is.read_any();
            try {
                push(arg0_in);
                _output = handler.createReply();
            } catch (org.omg.CosEventComm.Disconnected _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosEventComm.DisconnectedHelper.write(_output, _exception);
            }
            return _output;
        } else if (opName.equals("disconnect_push_consumer")) {
            disconnect_push_consumer();
            _output = handler.createReply();
            return _output;
        } else if (opName.equals("get_typed_consumer")) {
            org.omg.CORBA.Object _arg_result = get_typed_consumer();
            _output = handler.createReply();
            _output.write_Object(_arg_result);
            return _output;
        } else throw new org.omg.CORBA.BAD_OPERATION();
    }
