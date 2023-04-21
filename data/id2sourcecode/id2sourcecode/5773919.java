    private org.omg.CORBA.portable.OutputStream _OB_op_suspend_connection(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream out = null;
        try {
            suspend_connection();
            out = handler.createReply();
        } catch (ConnectionAlreadyInactive _ob_ex) {
            out = handler.createExceptionReply();
            ConnectionAlreadyInactiveHelper.write(out, _ob_ex);
        } catch (NotConnected _ob_ex) {
            out = handler.createExceptionReply();
            NotConnectedHelper.write(out, _ob_ex);
        }
        return out;
    }
