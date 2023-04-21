    public fr.esrf.Tango.AttributeValue_4[] write_read_attributes_4(fr.esrf.Tango.AttributeValue_4[] values, fr.esrf.Tango.ClntIdent cl_ident) throws fr.esrf.Tango.MultiDevFailed, fr.esrf.Tango.DevFailed {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("write_read_attributes_4", true);
                    fr.esrf.Tango.AttributeValueList_4Helper.write(_os, values);
                    fr.esrf.Tango.ClntIdentHelper.write(_os, cl_ident);
                    _is = _invoke(_os);
                    fr.esrf.Tango.AttributeValue_4[] _result = fr.esrf.Tango.AttributeValueList_4Helper.read(_is);
                    return _result;
                } catch (org.omg.CORBA.portable.RemarshalException _rx) {
                } catch (org.omg.CORBA.portable.ApplicationException _ax) {
                    String _id = _ax.getId();
                    if (_id.equals("IDL:Tango/MultiDevFailed:1.0")) {
                        throw fr.esrf.Tango.MultiDevFailedHelper.read(_ax.getInputStream());
                    } else if (_id.equals("IDL:Tango/DevFailed:1.0")) {
                        throw fr.esrf.Tango.DevFailedHelper.read(_ax.getInputStream());
                    } else throw new RuntimeException("Unexpected exception " + _id);
                } finally {
                    this._releaseReply(_is);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("write_read_attributes_4", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                Device_4Operations _localServant = (Device_4Operations) _so.servant;
                fr.esrf.Tango.AttributeValue_4[] _result;
                try {
                    _result = _localServant.write_read_attributes_4(values, cl_ident);
                } finally {
                    _servant_postinvoke(_so);
                }
                return _result;
            }
        }
    }
