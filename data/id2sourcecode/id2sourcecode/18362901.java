    public org.omg.CORBA.portable.OutputStream _invoke(String method, org.omg.CORBA.portable.InputStream _input, org.omg.CORBA.portable.ResponseHandler handler) throws org.omg.CORBA.SystemException {
        org.omg.CORBA.portable.OutputStream _out = null;
        java.lang.Integer opsIndex = (java.lang.Integer) m_opsHash.get(method);
        if (null == opsIndex) throw new org.omg.CORBA.BAD_OPERATION(method + " not found");
        switch(opsIndex.intValue()) {
            case 0:
                {
                    try {
                        org.omg.CORBA.Object _arg0 = _input.read_Object();
                        org.omg.CosNaming.NameComponent[] _arg1 = org.omg.CORBA.FT.LocationHelper.read(_input);
                        _out = handler.createReply();
                        _out.write_Object(set_primary_member(_arg0, _arg1));
                    } catch (org.omg.CORBA.FT.PrimaryNotSet _ex0) {
                        _out = handler.createExceptionReply();
                        org.omg.CORBA.FT.PrimaryNotSetHelper.write(_out, _ex0);
                    } catch (org.omg.CORBA.FT.MemberNotFound _ex1) {
                        _out = handler.createExceptionReply();
                        org.omg.CORBA.FT.MemberNotFoundHelper.write(_out, _ex1);
                    } catch (org.omg.CORBA.FT.BadReplicationStyle _ex2) {
                        _out = handler.createExceptionReply();
                        org.omg.CORBA.FT.BadReplicationStyleHelper.write(_out, _ex2);
                    } catch (org.omg.CORBA.FT.ObjectGroupNotFound _ex3) {
                        _out = handler.createExceptionReply();
                        org.omg.CORBA.FT.ObjectGroupNotFoundHelper.write(_out, _ex3);
                    }
                    break;
                }
            case 1:
                {
                    try {
                        org.omg.CORBA.Object _arg0 = _input.read_Object();
                        _out = handler.createReply();
                        org.omg.CORBA.FT.LocationsHelper.write(_out, locations_of_members(_arg0));
                    } catch (org.omg.CORBA.FT.ObjectGroupNotFound _ex0) {
                        _out = handler.createExceptionReply();
                        org.omg.CORBA.FT.ObjectGroupNotFoundHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 2:
                {
                    try {
                        org.omg.CORBA.Object _arg0 = _input.read_Object();
                        org.omg.CosNaming.NameComponent[] _arg1 = org.omg.CORBA.FT.LocationHelper.read(_input);
                        _out = handler.createReply();
                        _out.write_Object(get_member_ref(_arg0, _arg1));
                    } catch (org.omg.CORBA.FT.MemberNotFound _ex0) {
                        _out = handler.createExceptionReply();
                        org.omg.CORBA.FT.MemberNotFoundHelper.write(_out, _ex0);
                    } catch (org.omg.CORBA.FT.ObjectGroupNotFound _ex1) {
                        _out = handler.createExceptionReply();
                        org.omg.CORBA.FT.ObjectGroupNotFoundHelper.write(_out, _ex1);
                    }
                    break;
                }
            case 3:
                {
                    try {
                        org.omg.CORBA.Object _arg0 = _input.read_Object();
                        org.omg.CosNaming.NameComponent[] _arg1 = org.omg.CORBA.FT.LocationHelper.read(_input);
                        java.lang.String _arg2 = org.omg.CORBA.FT.TypeIdHelper.read(_input);
                        org.omg.CORBA.FT.Property[] _arg3 = org.omg.CORBA.FT.CriteriaHelper.read(_input);
                        _out = handler.createReply();
                        _out.write_Object(create_member(_arg0, _arg1, _arg2, _arg3));
                    } catch (org.omg.CORBA.FT.ObjectNotCreated _ex0) {
                        _out = handler.createExceptionReply();
                        org.omg.CORBA.FT.ObjectNotCreatedHelper.write(_out, _ex0);
                    } catch (org.omg.CORBA.FT.MemberAlreadyPresent _ex1) {
                        _out = handler.createExceptionReply();
                        org.omg.CORBA.FT.MemberAlreadyPresentHelper.write(_out, _ex1);
                    } catch (org.omg.CORBA.FT.CannotMeetCriteria _ex2) {
                        _out = handler.createExceptionReply();
                        org.omg.CORBA.FT.CannotMeetCriteriaHelper.write(_out, _ex2);
                    } catch (org.omg.CORBA.FT.ObjectGroupNotFound _ex3) {
                        _out = handler.createExceptionReply();
                        org.omg.CORBA.FT.ObjectGroupNotFoundHelper.write(_out, _ex3);
                    } catch (org.omg.CORBA.FT.InvalidCriteria _ex4) {
                        _out = handler.createExceptionReply();
                        org.omg.CORBA.FT.InvalidCriteriaHelper.write(_out, _ex4);
                    } catch (org.omg.CORBA.FT.NoFactory _ex5) {
                        _out = handler.createExceptionReply();
                        org.omg.CORBA.FT.NoFactoryHelper.write(_out, _ex5);
                    }
                    break;
                }
            case 4:
                {
                    try {
                        org.omg.CORBA.Object _arg0 = _input.read_Object();
                        _out = handler.createReply();
                        _out.write_ulonglong(get_object_group_id(_arg0));
                    } catch (org.omg.CORBA.FT.ObjectGroupNotFound _ex0) {
                        _out = handler.createExceptionReply();
                        org.omg.CORBA.FT.ObjectGroupNotFoundHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 5:
                {
                    try {
                        org.omg.CORBA.Object _arg0 = _input.read_Object();
                        _out = handler.createReply();
                        _out.write_Object(get_object_group_ref(_arg0));
                    } catch (org.omg.CORBA.FT.ObjectGroupNotFound _ex0) {
                        _out = handler.createExceptionReply();
                        org.omg.CORBA.FT.ObjectGroupNotFoundHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 6:
                {
                    try {
                        org.omg.CORBA.Object _arg0 = _input.read_Object();
                        org.omg.CosNaming.NameComponent[] _arg1 = org.omg.CORBA.FT.LocationHelper.read(_input);
                        _out = handler.createReply();
                        _out.write_Object(remove_member(_arg0, _arg1));
                    } catch (org.omg.CORBA.FT.MemberNotFound _ex0) {
                        _out = handler.createExceptionReply();
                        org.omg.CORBA.FT.MemberNotFoundHelper.write(_out, _ex0);
                    } catch (org.omg.CORBA.FT.ObjectGroupNotFound _ex1) {
                        _out = handler.createExceptionReply();
                        org.omg.CORBA.FT.ObjectGroupNotFoundHelper.write(_out, _ex1);
                    }
                    break;
                }
            case 7:
                {
                    try {
                        org.omg.CORBA.Object _arg0 = _input.read_Object();
                        org.omg.CosNaming.NameComponent[] _arg1 = org.omg.CORBA.FT.LocationHelper.read(_input);
                        org.omg.CORBA.Object _arg2 = _input.read_Object();
                        _out = handler.createReply();
                        _out.write_Object(add_member(_arg0, _arg1, _arg2));
                    } catch (org.omg.CORBA.FT.ObjectNotAdded _ex0) {
                        _out = handler.createExceptionReply();
                        org.omg.CORBA.FT.ObjectNotAddedHelper.write(_out, _ex0);
                    } catch (org.omg.CORBA.INV_OBJREF _ex1) {
                        _out = handler.createExceptionReply();
                        org.omg.CORBA.INV_OBJREFHelper.write(_out, _ex1);
                    } catch (org.omg.CORBA.FT.MemberAlreadyPresent _ex2) {
                        _out = handler.createExceptionReply();
                        org.omg.CORBA.FT.MemberAlreadyPresentHelper.write(_out, _ex2);
                    } catch (org.omg.CORBA.FT.ObjectGroupNotFound _ex3) {
                        _out = handler.createExceptionReply();
                        org.omg.CORBA.FT.ObjectGroupNotFoundHelper.write(_out, _ex3);
                    }
                    break;
                }
        }
        return _out;
    }
