    OutputStream super_invoke(String method, InputStream in, ResponseHandler rh) {
        OutputStream out = null;
        Integer call_method = (Integer) _NamingContextImplBase.methods.get(method);
        if (call_method == null) throw new BAD_OPERATION(Minor.Method, CompletionStatus.COMPLETED_MAYBE);
        switch(call_method.intValue()) {
            case 0:
                {
                    try {
                        NameComponent[] a_name = NameHelper.read(in);
                        org.omg.CORBA.Object an_object = ObjectHelper.read(in);
                        bind(a_name, an_object);
                        out = rh.createReply();
                    } catch (NotFound ex) {
                        out = rh.createExceptionReply();
                        NotFoundHelper.write(out, ex);
                    } catch (CannotProceed ex) {
                        out = rh.createExceptionReply();
                        CannotProceedHelper.write(out, ex);
                    } catch (InvalidName ex) {
                        out = rh.createExceptionReply();
                        InvalidNameHelper.write(out, ex);
                    } catch (AlreadyBound ex) {
                        out = rh.createExceptionReply();
                        AlreadyBoundHelper.write(out, ex);
                    }
                    break;
                }
            case 1:
                {
                    try {
                        NameComponent[] a_name = NameHelper.read(in);
                        org.omg.CORBA.Object an_object = ObjectHelper.read(in);
                        rebind(a_name, an_object);
                        out = rh.createReply();
                    } catch (NotFound ex) {
                        out = rh.createExceptionReply();
                        NotFoundHelper.write(out, ex);
                    } catch (CannotProceed ex) {
                        out = rh.createExceptionReply();
                        CannotProceedHelper.write(out, ex);
                    } catch (InvalidName ex) {
                        out = rh.createExceptionReply();
                        InvalidNameHelper.write(out, ex);
                    }
                    break;
                }
            case 2:
                {
                    try {
                        NameComponent[] a_name = NameHelper.read(in);
                        NamingContext a_context = NamingContextHelper.read(in);
                        bind_context(a_name, a_context);
                        out = rh.createReply();
                    } catch (NotFound ex) {
                        out = rh.createExceptionReply();
                        NotFoundHelper.write(out, ex);
                    } catch (CannotProceed ex) {
                        out = rh.createExceptionReply();
                        CannotProceedHelper.write(out, ex);
                    } catch (InvalidName ex) {
                        out = rh.createExceptionReply();
                        InvalidNameHelper.write(out, ex);
                    } catch (AlreadyBound ex) {
                        out = rh.createExceptionReply();
                        AlreadyBoundHelper.write(out, ex);
                    }
                    break;
                }
            case 3:
                {
                    try {
                        NameComponent[] a_name = NameHelper.read(in);
                        NamingContext a_context = NamingContextHelper.read(in);
                        rebind_context(a_name, a_context);
                        out = rh.createReply();
                    } catch (NotFound ex) {
                        out = rh.createExceptionReply();
                        NotFoundHelper.write(out, ex);
                    } catch (CannotProceed ex) {
                        out = rh.createExceptionReply();
                        CannotProceedHelper.write(out, ex);
                    } catch (InvalidName ex) {
                        out = rh.createExceptionReply();
                        InvalidNameHelper.write(out, ex);
                    }
                    break;
                }
            case 4:
                {
                    try {
                        NameComponent[] a_name = NameHelper.read(in);
                        org.omg.CORBA.Object __result = null;
                        __result = resolve(a_name);
                        out = rh.createReply();
                        ObjectHelper.write(out, __result);
                    } catch (NotFound ex) {
                        out = rh.createExceptionReply();
                        NotFoundHelper.write(out, ex);
                    } catch (CannotProceed ex) {
                        out = rh.createExceptionReply();
                        CannotProceedHelper.write(out, ex);
                    } catch (InvalidName ex) {
                        out = rh.createExceptionReply();
                        InvalidNameHelper.write(out, ex);
                    }
                    break;
                }
            case 5:
                {
                    try {
                        NameComponent[] a_name = NameHelper.read(in);
                        unbind(a_name);
                        out = rh.createReply();
                    } catch (NotFound ex) {
                        out = rh.createExceptionReply();
                        NotFoundHelper.write(out, ex);
                    } catch (CannotProceed ex) {
                        out = rh.createExceptionReply();
                        CannotProceedHelper.write(out, ex);
                    } catch (InvalidName ex) {
                        out = rh.createExceptionReply();
                        InvalidNameHelper.write(out, ex);
                    }
                    break;
                }
            case 6:
                {
                    NamingContext __result = null;
                    __result = new_context();
                    out = rh.createReply();
                    NamingContextHelper.write(out, __result);
                    break;
                }
            case 7:
                {
                    try {
                        NameComponent[] a_name = NameHelper.read(in);
                        NamingContext __result = null;
                        __result = bind_new_context(a_name);
                        out = rh.createReply();
                        NamingContextHelper.write(out, __result);
                    } catch (NotFound ex) {
                        out = rh.createExceptionReply();
                        NotFoundHelper.write(out, ex);
                    } catch (AlreadyBound ex) {
                        out = rh.createExceptionReply();
                        AlreadyBoundHelper.write(out, ex);
                    } catch (CannotProceed ex) {
                        out = rh.createExceptionReply();
                        CannotProceedHelper.write(out, ex);
                    } catch (InvalidName ex) {
                        out = rh.createExceptionReply();
                        InvalidNameHelper.write(out, ex);
                    }
                    break;
                }
            case 8:
                {
                    try {
                        destroy();
                        out = rh.createReply();
                    } catch (NotEmpty ex) {
                        out = rh.createExceptionReply();
                        NotEmptyHelper.write(out, ex);
                    }
                    break;
                }
            case 9:
                {
                    int amount = in.read_ulong();
                    BindingListHolder a_list = new BindingListHolder();
                    BindingIteratorHolder an_iter = new BindingIteratorHolder();
                    list(amount, a_list, an_iter);
                    out = rh.createReply();
                    BindingListHelper.write(out, a_list.value);
                    BindingIteratorHelper.write(out, an_iter.value);
                    break;
                }
            default:
                throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
        }
        return out;
    }
