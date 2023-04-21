    public org.omg.CORBA.portable.OutputStream _invoke(String $method, org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler $rh) {
        org.omg.CORBA.portable.OutputStream out = null;
        java.lang.Integer __method = (java.lang.Integer) _methods.get($method);
        if (__method == null) throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
        switch(__method.intValue()) {
            case 0:
                {
                    try {
                        CosEventComm.PullConsumer pull_consumer = CosEventComm.PullConsumerHelper.read(in);
                        this.connect_pull_consumer(pull_consumer);
                        out = $rh.createReply();
                    } catch (CosEventChannelAdmin.AlreadyConnected $ex) {
                        out = $rh.createExceptionReply();
                        CosEventChannelAdmin.AlreadyConnectedHelper.write(out, $ex);
                    } catch (CosEventChannelAdmin.TypeError $ex) {
                        out = $rh.createExceptionReply();
                        CosEventChannelAdmin.TypeErrorHelper.write(out, $ex);
                    }
                    break;
                }
            case 1:
                {
                    try {
                        org.omg.CORBA.Any $result = null;
                        $result = this.pull();
                        out = $rh.createReply();
                        out.write_any($result);
                    } catch (CosEventComm.Disconnected $ex) {
                        out = $rh.createExceptionReply();
                        CosEventComm.DisconnectedHelper.write(out, $ex);
                    }
                    break;
                }
            case 2:
                {
                    try {
                        org.omg.CORBA.BooleanHolder has_event = new org.omg.CORBA.BooleanHolder();
                        org.omg.CORBA.Any $result = null;
                        $result = this.try_pull(has_event);
                        out = $rh.createReply();
                        out.write_any($result);
                        out.write_boolean(has_event.value);
                    } catch (CosEventComm.Disconnected $ex) {
                        out = $rh.createExceptionReply();
                        CosEventComm.DisconnectedHelper.write(out, $ex);
                    }
                    break;
                }
            case 3:
                {
                    this.disconnect_pull_supplier();
                    out = $rh.createReply();
                    break;
                }
            default:
                throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
        }
        return out;
    }
