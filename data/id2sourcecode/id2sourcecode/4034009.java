    public org.omg.CORBA.portable.OutputStream _invoke(String opName, org.omg.CORBA.portable.InputStream _is, org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output = null;
        if (opName.equals("_get_incarnation")) {
            org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.IncarnationNumber arg = incarnation();
            _output = handler.createReply();
            org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.IncarnationNumberHelper.write(_output, arg);
            return _output;
        } else if (opName.equals("add_type")) {
            java.lang.String arg0_in = org.omg.CosTrading.ServiceTypeNameHelper.read(_is);
            java.lang.String arg1_in = org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.IdentifierHelper.read(_is);
            org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.PropStruct[] arg2_in = org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.PropStructSeqHelper.read(_is);
            java.lang.String[] arg3_in = org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.ServiceTypeNameSeqHelper.read(_is);
            try {
                org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.IncarnationNumber _arg_result = add_type(arg0_in, arg1_in, arg2_in, arg3_in);
                _output = handler.createReply();
                org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.IncarnationNumberHelper.write(_output, _arg_result);
            } catch (org.omg.CosTrading.IllegalServiceType _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosTrading.IllegalServiceTypeHelper.write(_output, _exception);
            } catch (org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.ServiceTypeExists _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.ServiceTypeExistsHelper.write(_output, _exception);
            } catch (org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.InterfaceTypeMismatch _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.InterfaceTypeMismatchHelper.write(_output, _exception);
            } catch (org.omg.CosTrading.IllegalPropertyName _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosTrading.IllegalPropertyNameHelper.write(_output, _exception);
            } catch (org.omg.CosTrading.DuplicatePropertyName _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosTrading.DuplicatePropertyNameHelper.write(_output, _exception);
            } catch (org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.ValueTypeRedefinition _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.ValueTypeRedefinitionHelper.write(_output, _exception);
            } catch (org.omg.CosTrading.UnknownServiceType _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosTrading.UnknownServiceTypeHelper.write(_output, _exception);
            } catch (org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.DuplicateServiceTypeName _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.DuplicateServiceTypeNameHelper.write(_output, _exception);
            }
            return _output;
        } else if (opName.equals("remove_type")) {
            java.lang.String arg0_in = org.omg.CosTrading.ServiceTypeNameHelper.read(_is);
            try {
                remove_type(arg0_in);
                _output = handler.createReply();
            } catch (org.omg.CosTrading.IllegalServiceType _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosTrading.IllegalServiceTypeHelper.write(_output, _exception);
            } catch (org.omg.CosTrading.UnknownServiceType _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosTrading.UnknownServiceTypeHelper.write(_output, _exception);
            } catch (org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.HasSubTypes _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.HasSubTypesHelper.write(_output, _exception);
            }
            return _output;
        } else if (opName.equals("list_types")) {
            org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.SpecifiedServiceTypes arg0_in = org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.SpecifiedServiceTypesHelper.read(_is);
            java.lang.String[] _arg_result = list_types(arg0_in);
            _output = handler.createReply();
            org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.ServiceTypeNameSeqHelper.write(_output, _arg_result);
            return _output;
        } else if (opName.equals("describe_type")) {
            java.lang.String arg0_in = org.omg.CosTrading.ServiceTypeNameHelper.read(_is);
            try {
                org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.TypeStruct _arg_result = describe_type(arg0_in);
                _output = handler.createReply();
                org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.TypeStructHelper.write(_output, _arg_result);
            } catch (org.omg.CosTrading.IllegalServiceType _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosTrading.IllegalServiceTypeHelper.write(_output, _exception);
            } catch (org.omg.CosTrading.UnknownServiceType _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosTrading.UnknownServiceTypeHelper.write(_output, _exception);
            }
            return _output;
        } else if (opName.equals("fully_describe_type")) {
            java.lang.String arg0_in = org.omg.CosTrading.ServiceTypeNameHelper.read(_is);
            try {
                org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.TypeStruct _arg_result = fully_describe_type(arg0_in);
                _output = handler.createReply();
                org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.TypeStructHelper.write(_output, _arg_result);
            } catch (org.omg.CosTrading.IllegalServiceType _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosTrading.IllegalServiceTypeHelper.write(_output, _exception);
            } catch (org.omg.CosTrading.UnknownServiceType _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosTrading.UnknownServiceTypeHelper.write(_output, _exception);
            }
            return _output;
        } else if (opName.equals("mask_type")) {
            java.lang.String arg0_in = org.omg.CosTrading.ServiceTypeNameHelper.read(_is);
            try {
                mask_type(arg0_in);
                _output = handler.createReply();
            } catch (org.omg.CosTrading.IllegalServiceType _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosTrading.IllegalServiceTypeHelper.write(_output, _exception);
            } catch (org.omg.CosTrading.UnknownServiceType _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosTrading.UnknownServiceTypeHelper.write(_output, _exception);
            } catch (org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.AlreadyMasked _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.AlreadyMaskedHelper.write(_output, _exception);
            }
            return _output;
        } else if (opName.equals("unmask_type")) {
            java.lang.String arg0_in = org.omg.CosTrading.ServiceTypeNameHelper.read(_is);
            try {
                unmask_type(arg0_in);
                _output = handler.createReply();
            } catch (org.omg.CosTrading.IllegalServiceType _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosTrading.IllegalServiceTypeHelper.write(_output, _exception);
            } catch (org.omg.CosTrading.UnknownServiceType _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosTrading.UnknownServiceTypeHelper.write(_output, _exception);
            } catch (org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.NotMasked _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosTradingRepos.ServiceTypeRepositoryPackage.NotMaskedHelper.write(_output, _exception);
            }
            return _output;
        } else throw new org.omg.CORBA.BAD_OPERATION();
    }
