    private org.omg.CORBA.portable.OutputStream _OB_op_connect_sequence_pull_consumer(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream out = null;
        try {
            org.omg.CosNotifyComm.SequencePullConsumer _ob_a0 = org.omg.CosNotifyComm.SequencePullConsumerHelper.read(in);
            connect_sequence_pull_consumer(_ob_a0);
            out = handler.createReply();
        } catch (org.omg.CosEventChannelAdmin.AlreadyConnected _ob_ex) {
            out = handler.createExceptionReply();
            org.omg.CosEventChannelAdmin.AlreadyConnectedHelper.write(out, _ob_ex);
        }
        return out;
    }
