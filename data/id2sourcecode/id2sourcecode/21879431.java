    public org.omg.CORBA.portable.OutputStream _invoke(String method, org.omg.CORBA.portable.InputStream _input, org.omg.CORBA.portable.ResponseHandler handler) throws org.omg.CORBA.SystemException {
        org.omg.CORBA.portable.OutputStream _out = null;
        java.lang.Integer opsIndex = (java.lang.Integer) m_opsHash.get(method);
        if (null == opsIndex) throw new org.omg.CORBA.BAD_OPERATION(method + " not found");
        switch(opsIndex.intValue()) {
            case 0:
                {
                    try {
                        fr.esrf.Tango.AttributeConfig_3[] _arg0 = fr.esrf.Tango.AttributeConfigList_3Helper.read(_input);
                        fr.esrf.Tango.ClntIdent _arg1 = fr.esrf.Tango.ClntIdentHelper.read(_input);
                        _out = handler.createReply();
                        set_attribute_config_4(_arg0, _arg1);
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 1:
                {
                    try {
                        fr.esrf.Tango.AttributeValue_4[] _arg0 = fr.esrf.Tango.AttributeValueList_4Helper.read(_input);
                        fr.esrf.Tango.ClntIdent _arg1 = fr.esrf.Tango.ClntIdentHelper.read(_input);
                        _out = handler.createReply();
                        fr.esrf.Tango.AttributeValueList_4Helper.write(_out, write_read_attributes_4(_arg0, _arg1));
                    } catch (fr.esrf.Tango.MultiDevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.MultiDevFailedHelper.write(_out, _ex0);
                    } catch (fr.esrf.Tango.DevFailed _ex1) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex1);
                    }
                    break;
                }
            case 2:
                {
                    try {
                        fr.esrf.Tango.AttributeValue[] _arg0 = fr.esrf.Tango.AttributeValueListHelper.read(_input);
                        _out = handler.createReply();
                        write_attributes_3(_arg0);
                    } catch (fr.esrf.Tango.MultiDevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.MultiDevFailedHelper.write(_out, _ex0);
                    } catch (fr.esrf.Tango.DevFailed _ex1) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex1);
                    }
                    break;
                }
            case 3:
                {
                    try {
                        _out = handler.createReply();
                        fr.esrf.Tango.DevInfoHelper.write(_out, info());
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 4:
                {
                    _out = handler.createReply();
                    _out.write_string(status());
                    break;
                }
            case 5:
                {
                    try {
                        fr.esrf.Tango.AttributeConfig_3[] _arg0 = fr.esrf.Tango.AttributeConfigList_3Helper.read(_input);
                        _out = handler.createReply();
                        set_attribute_config_3(_arg0);
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 6:
                {
                    _out = handler.createReply();
                    _out.write_string(name());
                    break;
                }
            case 7:
                {
                    try {
                        java.lang.String[] _arg0 = fr.esrf.Tango.DevVarStringArrayHelper.read(_input);
                        _out = handler.createReply();
                        fr.esrf.Tango.AttributeValueListHelper.write(_out, read_attributes(_arg0));
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 8:
                {
                    try {
                        java.lang.String _arg0 = _input.read_string();
                        int _arg1 = _input.read_long();
                        _out = handler.createReply();
                        fr.esrf.Tango.DevAttrHistoryList_3Helper.write(_out, read_attribute_history_3(_arg0, _arg1));
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 9:
                {
                    try {
                        java.lang.String[] _arg0 = fr.esrf.Tango.DevVarStringArrayHelper.read(_input);
                        fr.esrf.Tango.DevSource _arg1 = fr.esrf.Tango.DevSourceHelper.read(_input);
                        _out = handler.createReply();
                        fr.esrf.Tango.AttributeValueListHelper.write(_out, read_attributes_2(_arg0, _arg1));
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 10:
                {
                    _out = handler.createReply();
                    _out.write_string(adm_name());
                    break;
                }
            case 11:
                {
                    try {
                        _out = handler.createReply();
                        fr.esrf.Tango.DevInfo_3Helper.write(_out, info_3());
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 12:
                {
                    try {
                        java.lang.String _arg0 = _input.read_string();
                        org.omg.CORBA.Any _arg1 = _input.read_any();
                        fr.esrf.Tango.DevSource _arg2 = fr.esrf.Tango.DevSourceHelper.read(_input);
                        _out = handler.createReply();
                        _out.write_any(command_inout_2(_arg0, _arg1, _arg2));
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 13:
                {
                    try {
                        java.lang.String[] _arg0 = fr.esrf.Tango.DevVarStringArrayHelper.read(_input);
                        _out = handler.createReply();
                        fr.esrf.Tango.AttributeConfigListHelper.write(_out, get_attribute_config(_arg0));
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 14:
                {
                    try {
                        java.lang.String _arg0 = _input.read_string();
                        int _arg1 = _input.read_long();
                        _out = handler.createReply();
                        fr.esrf.Tango.DevAttrHistoryListHelper.write(_out, read_attribute_history_2(_arg0, _arg1));
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 15:
                {
                    try {
                        _out = handler.createReply();
                        fr.esrf.Tango.DevCmdInfoListHelper.write(_out, command_list_query());
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 16:
                {
                    try {
                        java.lang.String[] _arg0 = fr.esrf.Tango.DevVarStringArrayHelper.read(_input);
                        _out = handler.createReply();
                        fr.esrf.Tango.AttributeConfigList_2Helper.write(_out, get_attribute_config_2(_arg0));
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 17:
                {
                    try {
                        fr.esrf.Tango.AttributeValue_4[] _arg0 = fr.esrf.Tango.AttributeValueList_4Helper.read(_input);
                        fr.esrf.Tango.ClntIdent _arg1 = fr.esrf.Tango.ClntIdentHelper.read(_input);
                        _out = handler.createReply();
                        write_attributes_4(_arg0, _arg1);
                    } catch (fr.esrf.Tango.MultiDevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.MultiDevFailedHelper.write(_out, _ex0);
                    } catch (fr.esrf.Tango.DevFailed _ex1) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex1);
                    }
                    break;
                }
            case 18:
                {
                    try {
                        java.lang.String[] _arg0 = fr.esrf.Tango.DevVarStringArrayHelper.read(_input);
                        _out = handler.createReply();
                        fr.esrf.Tango.AttributeConfigList_3Helper.write(_out, get_attribute_config_3(_arg0));
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 19:
                {
                    try {
                        _out = handler.createReply();
                        fr.esrf.Tango.DevCmdInfoList_2Helper.write(_out, command_list_query_2());
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 20:
                {
                    _out = handler.createReply();
                    fr.esrf.Tango.DevStateHelper.write(_out, state());
                    break;
                }
            case 21:
                {
                    try {
                        java.lang.String _arg0 = _input.read_string();
                        int _arg1 = _input.read_long();
                        _out = handler.createReply();
                        fr.esrf.Tango.DevAttrHistory_4Helper.write(_out, read_attribute_history_4(_arg0, _arg1));
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 22:
                {
                    try {
                        java.lang.String _arg0 = _input.read_string();
                        int _arg1 = _input.read_long();
                        _out = handler.createReply();
                        fr.esrf.Tango.DevCmdHistoryListHelper.write(_out, command_inout_history_2(_arg0, _arg1));
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 23:
                {
                    try {
                        _out = handler.createReply();
                        ping();
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 24:
                {
                    try {
                        fr.esrf.Tango.AttributeValue[] _arg0 = fr.esrf.Tango.AttributeValueListHelper.read(_input);
                        _out = handler.createReply();
                        write_attributes(_arg0);
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 25:
                {
                    try {
                        java.lang.String _arg0 = _input.read_string();
                        org.omg.CORBA.Any _arg1 = _input.read_any();
                        _out = handler.createReply();
                        _out.write_any(command_inout(_arg0, _arg1));
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 26:
                {
                    try {
                        java.lang.String _arg0 = _input.read_string();
                        int _arg1 = _input.read_long();
                        _out = handler.createReply();
                        fr.esrf.Tango.DevCmdHistory_4Helper.write(_out, command_inout_history_4(_arg0, _arg1));
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 27:
                {
                    try {
                        java.lang.String _arg0 = _input.read_string();
                        _out = handler.createReply();
                        fr.esrf.Tango.DevCmdInfoHelper.write(_out, command_query(_arg0));
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 28:
                {
                    try {
                        java.lang.String _arg0 = _input.read_string();
                        _out = handler.createReply();
                        fr.esrf.Tango.DevCmdInfo_2Helper.write(_out, command_query_2(_arg0));
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 29:
                {
                    _out = handler.createReply();
                    _out.write_string(description());
                    break;
                }
            case 30:
                {
                    try {
                        java.lang.String[] _arg0 = fr.esrf.Tango.DevVarStringArrayHelper.read(_input);
                        fr.esrf.Tango.DevSource _arg1 = fr.esrf.Tango.DevSourceHelper.read(_input);
                        _out = handler.createReply();
                        fr.esrf.Tango.AttributeValueList_3Helper.write(_out, read_attributes_3(_arg0, _arg1));
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 31:
                {
                    try {
                        java.lang.String[] _arg0 = fr.esrf.Tango.DevVarStringArrayHelper.read(_input);
                        fr.esrf.Tango.DevSource _arg1 = fr.esrf.Tango.DevSourceHelper.read(_input);
                        fr.esrf.Tango.ClntIdent _arg2 = fr.esrf.Tango.ClntIdentHelper.read(_input);
                        _out = handler.createReply();
                        fr.esrf.Tango.AttributeValueList_4Helper.write(_out, read_attributes_4(_arg0, _arg1, _arg2));
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 32:
                {
                    try {
                        int _arg0 = _input.read_long();
                        _out = handler.createReply();
                        fr.esrf.Tango.DevVarStringArrayHelper.write(_out, black_box(_arg0));
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 33:
                {
                    try {
                        fr.esrf.Tango.AttributeConfig[] _arg0 = fr.esrf.Tango.AttributeConfigListHelper.read(_input);
                        _out = handler.createReply();
                        set_attribute_config(_arg0);
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 34:
                {
                    try {
                        java.lang.String _arg0 = _input.read_string();
                        org.omg.CORBA.Any _arg1 = _input.read_any();
                        fr.esrf.Tango.DevSource _arg2 = fr.esrf.Tango.DevSourceHelper.read(_input);
                        fr.esrf.Tango.ClntIdent _arg3 = fr.esrf.Tango.ClntIdentHelper.read(_input);
                        _out = handler.createReply();
                        _out.write_any(command_inout_4(_arg0, _arg1, _arg2, _arg3));
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
        }
        return _out;
    }
