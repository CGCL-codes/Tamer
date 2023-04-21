    public void cmdProc(Interp interp, TclObject argv[]) throws TclException {
        int lastArg = argv.length - 1;
        boolean statOpt = false;
        boolean typeOpt = false;
        TclObject resultListObj;
        Class c;
        if (argv.length < 2) {
            throw new TclNumArgsException(interp, 1, argv, "option ?arg arg ...?");
        }
        int opt = TclIndex.get(interp, argv[1], validCmds, "option", 0);
        switch(opt) {
            case BASECLASS:
                if (argv.length != 3) {
                    throw new TclNumArgsException(interp, 2, argv, "objOrClass");
                }
                c = getClassFromObj(interp, argv[2]);
                if (c != null) {
                    interp.setResult(getBaseNameFromClass(c));
                }
                return;
            case CLASS:
                if (argv.length != 3) {
                    throw new TclNumArgsException(interp, 2, argv, "javaObj");
                }
                c = ReflectObject.getClass(interp, argv[2]);
                if (c != null) {
                    interp.setResult(getNameFromClass(c));
                }
                return;
            case DIMENSIONS:
                if (argv.length != 3) {
                    throw new TclNumArgsException(interp, 2, argv, "objOrClass");
                }
                c = getClassFromObj(interp, argv[2]);
                if (c == null) {
                    interp.setResult(0);
                } else {
                    interp.setResult(getNumDimsFromClass(c));
                }
                return;
            case EVENTS:
                if (argv.length != 3) {
                    throw new TclNumArgsException(interp, 2, argv, "javaObj");
                }
                c = getClassFromObj(interp, argv[2]);
                if (c == null) {
                    interp.resetResult();
                    return;
                }
                if (!PkgInvoker.isAccessible(c)) {
                    JavaInvoke.notAccessibleError(interp, c);
                }
                lookup: {
                    BeanInfo beanInfo;
                    try {
                        beanInfo = Introspector.getBeanInfo(c);
                    } catch (IntrospectionException e) {
                        break lookup;
                    }
                    EventSetDescriptor events[] = beanInfo.getEventSetDescriptors();
                    if (events == null) {
                        break lookup;
                    }
                    TclObject list = TclList.newInstance();
                    for (int i = 0; i < events.length; i++) {
                        TclList.append(interp, list, TclString.newInstance(getNameFromClass(events[i].getListenerType())));
                    }
                    interp.setResult(list);
                    return;
                }
                interp.resetResult();
                return;
            case FIELDS:
                if ((lastArg < 2) || (lastArg > 4)) {
                    throw new TclNumArgsException(interp, 2, argv, "?-type? ?-static? objOrClass");
                }
                for (int i = 2; i < lastArg; i++) {
                    opt = TclIndex.get(interp, argv[i], methOpts, "option", 0);
                    switch(opt) {
                        case STATIC_OPT:
                            statOpt = true;
                            break;
                        case TYPE_OPT:
                            typeOpt = true;
                            break;
                    }
                }
                c = getClassFromObj(interp, argv[lastArg]);
                if (c != null) {
                    if (!PkgInvoker.isAccessible(c)) {
                        JavaInvoke.notAccessibleError(interp, c);
                    }
                    resultListObj = getFieldInfoList(interp, c, statOpt, typeOpt);
                    interp.setResult(resultListObj);
                }
                return;
            case METHODS:
                if ((lastArg < 2) || (lastArg > 4)) {
                    throw new TclNumArgsException(interp, 2, argv, "?-type? ?-static? objOrClass");
                }
                for (int i = 2; i < lastArg; i++) {
                    opt = TclIndex.get(interp, argv[i], methOpts, "option", 0);
                    switch(opt) {
                        case STATIC_OPT:
                            statOpt = true;
                            break;
                        case TYPE_OPT:
                            typeOpt = true;
                            break;
                    }
                }
                c = getClassFromObj(interp, argv[lastArg]);
                if (c != null) {
                    if (!PkgInvoker.isAccessible(c)) {
                        JavaInvoke.notAccessibleError(interp, c);
                    }
                    resultListObj = getMethodInfoList(interp, c, statOpt, typeOpt);
                    interp.setResult(resultListObj);
                }
                return;
            case CONSTRUCTORS:
                if (argv.length != 3) {
                    throw new TclNumArgsException(interp, 2, argv, "objOrClass");
                }
                c = getClassFromObj(interp, argv[lastArg]);
                if (c != null) {
                    if (!PkgInvoker.isAccessible(c)) {
                        JavaInvoke.notAccessibleError(interp, c);
                    }
                    resultListObj = getConstructorInfoList(interp, c);
                    interp.setResult(resultListObj);
                }
                return;
            case PROPERTIES:
                if ((lastArg < 2) || (lastArg > 3)) {
                    throw new TclNumArgsException(interp, 2, argv, "?-type? objOrClass");
                }
                if (lastArg == 3) {
                    opt = TclIndex.get(interp, argv[2], propOpts, "option", 0);
                    typeOpt = true;
                }
                c = getClassFromObj(interp, argv[lastArg]);
                if (c != null) {
                    if (!PkgInvoker.isAccessible(c)) {
                        JavaInvoke.notAccessibleError(interp, c);
                    }
                    resultListObj = getPropInfoList(interp, c, typeOpt);
                    interp.setResult(resultListObj);
                }
                return;
            case SUPERCLASS:
                if (argv.length != 3) {
                    throw new TclNumArgsException(interp, 2, argv, "objOrClass");
                }
                c = getClassFromObj(interp, argv[2]);
                interp.resetResult();
                if (c != null) {
                    c = c.getSuperclass();
                    if (c != null) {
                        interp.setResult(getNameFromClass(c));
                    }
                }
                return;
        }
    }
