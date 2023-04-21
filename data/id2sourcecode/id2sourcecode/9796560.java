    public org.omg.CORBA.portable.OutputStream _invoke(String opName, org.omg.CORBA.portable.InputStream _is, org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output = null;
        if (opName.equals("connect_pull_supplier")) {
            org.omg.CosEventComm.PullSupplier arg0_in = org.omg.CosEventComm.PullSupplierHelper.read(_is);
            try {
                connect_pull_supplier(arg0_in);
                _output = handler.createReply();
            } catch (org.omg.CosEventChannelAdmin.AlreadyConnected _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosEventChannelAdmin.AlreadyConnectedHelper.write(_output, _exception);
            } catch (org.omg.CosEventChannelAdmin.TypeError _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosEventChannelAdmin.TypeErrorHelper.write(_output, _exception);
            }
            return _output;
        } else if (opName.equals("disconnect_pull_consumer")) {
            disconnect_pull_consumer();
            _output = handler.createReply();
            return _output;
        } else throw new org.omg.CORBA.BAD_OPERATION();
    }
