    public void cosRegisterCorbaServer(java.lang.String uniqueKey, tcg.syscontrol.cos.ICosMonitoredThread monitoredThread) throws tcg.syscontrol.cos.CosFailedToRegisterException {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("cosRegisterCorbaServer", true);
                    _os.write_string(uniqueKey);
                    tcg.syscontrol.cos.ICosMonitoredThreadHelper.write(_os, monitoredThread);
                    _is = _invoke(_os);
                    return;
                } catch (org.omg.CORBA.portable.RemarshalException _rx) {
                } catch (org.omg.CORBA.portable.ApplicationException _ax) {
                    String _id = _ax.getId();
                    if (_id.equals("IDL:tcg/syscontrol/cos/CosFailedToRegisterException:1.0")) {
                        throw tcg.syscontrol.cos.CosFailedToRegisterExceptionHelper.read(_ax.getInputStream());
                    }
                    throw new RuntimeException("Unexpected exception " + _id);
                } finally {
                    this._releaseReply(_is);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("cosRegisterCorbaServer", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                ICosProcessManagerOperations _localServant = (ICosProcessManagerOperations) _so.servant;
                try {
                    _localServant.cosRegisterCorbaServer(uniqueKey, monitoredThread);
                } finally {
                    _servant_postinvoke(_so);
                }
                return;
            }
        }
    }
