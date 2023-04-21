    public org.omg.CORBA.portable.OutputStream _invoke(String $method, org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler $rh) {
        org.omg.CORBA.portable.OutputStream out = null;
        java.lang.Integer __method = (java.lang.Integer) _methods.get($method);
        if (__method == null) throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
        switch(__method.intValue()) {
            case 0:
                {
                    try {
                        CosEventComm.PullSupplier pull_supplier = CosEventComm.PullSupplierHelper.read(in);
                        this.connect_pull_supplier(pull_supplier);
                        out = $rh.createReply();
                    } catch (CosEventChannelAdmin.AlreadyConnected $ex) {
                        out = $rh.createExceptionReply();
                        CosEventChannelAdmin.AlreadyConnectedHelper.write(out, $ex);
                    }
                    break;
                }
            case 1:
                {
                    this.disconnect_pull_consumer();
                    out = $rh.createReply();
                    break;
                }
            default:
                throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
        }
        return out;
    }
