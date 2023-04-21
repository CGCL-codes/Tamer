    public void cmdProc(Interp interp, TclObject[] objv) throws TclException {
        if (objv.length < 2) {
            throw new TclNumArgsException(interp, 1, objv, "cmd ?arg ...?");
        }
        int cmd = TclIndex.get(interp, objv[1], options, "option", 0);
        switch(cmd) {
            case OPT_ALIAS:
                {
                    if (objv.length >= 4) {
                        Interp slaveInterp = getInterp(interp, objv[2]);
                        if (objv.length == 4) {
                            InterpAliasCmd.describe(interp, slaveInterp, objv[3]);
                            return;
                        }
                        if ((objv.length == 5) && ("".equals(objv[4].toString()))) {
                            InterpAliasCmd.delete(interp, slaveInterp, objv[3]);
                            return;
                        }
                        if (objv.length > 5) {
                            Interp masterInterp = getInterp(interp, objv[4]);
                            if ("".equals(objv[5].toString())) {
                                if (objv.length == 6) {
                                    InterpAliasCmd.delete(interp, slaveInterp, objv[3]);
                                    return;
                                }
                            } else {
                                InterpAliasCmd.create(interp, slaveInterp, masterInterp, objv[3], objv[5], 6, objv);
                                return;
                            }
                        }
                    }
                    throw new TclNumArgsException(interp, 2, objv, "slavePath slaveCmd ?masterPath masterCmd? ?args ..?");
                }
            case OPT_ALIASES:
                {
                    Interp slaveInterp = getInterp(interp, objv);
                    InterpAliasCmd.list(interp, slaveInterp);
                    break;
                }
            case OPT_CREATE:
                {
                    boolean safe = interp.isSafe;
                    TclObject slaveNameObj = null;
                    boolean last = false;
                    for (int i = 2; i < objv.length; i++) {
                        if ((!last) && (objv[i].toString().charAt(0) == '-')) {
                            int index = TclIndex.get(interp, objv[i], createOptions, "option", 0);
                            if (index == OPT_CREATE_SAFE) {
                                safe = true;
                                continue;
                            }
                            i++;
                            last = true;
                        }
                        if (slaveNameObj != null) {
                            throw new TclNumArgsException(interp, 2, objv, "?-safe? ?--? ?path?");
                        }
                        slaveNameObj = objv[i];
                    }
                    if (slaveNameObj == null) {
                        int i = 0;
                        while (interp.getCommand("interp" + i) != null) {
                            i++;
                        }
                        slaveNameObj = TclString.newInstance("interp" + i);
                    }
                    InterpSlaveCmd.create(interp, slaveNameObj, safe);
                    interp.setResult(slaveNameObj);
                    break;
                }
            case OPT_DELETE:
                {
                    for (int i = 2; i < objv.length; i++) {
                        Interp slaveInterp = getInterp(interp, objv[i]);
                        if (slaveInterp == interp) {
                            throw new TclException(interp, "cannot delete the current interpreter");
                        }
                        InterpSlaveCmd slave = slaveInterp.slave;
                        slave.masterInterp.deleteCommandFromToken(slave.interpCmd);
                    }
                    break;
                }
            case OPT_EVAL:
                {
                    if (objv.length < 4) {
                        throw new TclNumArgsException(interp, 2, objv, "path arg ?arg ...?");
                    }
                    Interp slaveInterp = getInterp(interp, objv[2]);
                    InterpSlaveCmd.eval(interp, slaveInterp, 3, objv);
                    break;
                }
            case OPT_EXISTS:
                {
                    boolean exists = true;
                    try {
                        getInterp(interp, objv);
                    } catch (TclException e) {
                        if (objv.length > 3) {
                            throw e;
                        }
                        exists = false;
                    }
                    interp.setResult(exists);
                    break;
                }
            case OPT_EXPOSE:
                {
                    if (objv.length < 4 || objv.length > 5) {
                        throw new TclNumArgsException(interp, 2, objv, "path hiddenCmdName ?cmdName?");
                    }
                    Interp slaveInterp = getInterp(interp, objv[2]);
                    InterpSlaveCmd.expose(interp, slaveInterp, 3, objv);
                    break;
                }
            case OPT_HIDE:
                {
                    if (objv.length < 4 || objv.length > 5) {
                        throw new TclNumArgsException(interp, 2, objv, "path cmdName ?hiddenCmdName?");
                    }
                    Interp slaveInterp = getInterp(interp, objv[2]);
                    InterpSlaveCmd.hide(interp, slaveInterp, 3, objv);
                    break;
                }
            case OPT_HIDDEN:
                {
                    Interp slaveInterp = getInterp(interp, objv);
                    InterpSlaveCmd.hidden(interp, slaveInterp);
                    break;
                }
            case OPT_ISSAFE:
                {
                    Interp slaveInterp = getInterp(interp, objv);
                    interp.setResult(slaveInterp.isSafe);
                    break;
                }
            case OPT_INVOKEHIDDEN:
                {
                    boolean global = false;
                    int i;
                    for (i = 3; i < objv.length; i++) {
                        if (objv[i].toString().charAt(0) != '-') {
                            break;
                        }
                        int index = TclIndex.get(interp, objv[i], hiddenOptions, "option", 0);
                        if (index == OPT_HIDDEN_GLOBAL) {
                            global = true;
                        } else {
                            i++;
                            break;
                        }
                    }
                    if (objv.length - i < 1) {
                        throw new TclNumArgsException(interp, 2, objv, "path ?-global? ?--? cmd ?arg ..?");
                    }
                    Interp slaveInterp = getInterp(interp, objv[2]);
                    InterpSlaveCmd.invokeHidden(interp, slaveInterp, global, i, objv);
                    break;
                }
            case OPT_MARKTRUSTED:
                {
                    if (objv.length != 3) {
                        throw new TclNumArgsException(interp, 2, objv, "path");
                    }
                    Interp slaveInterp = getInterp(interp, objv[2]);
                    InterpSlaveCmd.markTrusted(interp, slaveInterp);
                    break;
                }
            case OPT_SLAVES:
                {
                    Interp slaveInterp = getInterp(interp, objv);
                    TclObject result = TclList.newInstance();
                    for (Iterator iter = slaveInterp.slaveTable.entrySet().iterator(); iter.hasNext(); ) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        String string = (String) entry.getKey();
                        TclList.append(interp, result, TclString.newInstance(string));
                    }
                    interp.setResult(result);
                    break;
                }
            case OPT_SHARE:
                {
                    if (objv.length != 5) {
                        throw new TclNumArgsException(interp, 2, objv, "srcPath channelId destPath");
                    }
                    Interp masterInterp = getInterp(interp, objv[2]);
                    Channel chan = TclIO.getChannel(masterInterp, objv[3].toString());
                    if (chan == null) {
                        throw new TclException(interp, "can not find channel named \"" + objv[3].toString() + "\"");
                    }
                    Interp slaveInterp = getInterp(interp, objv[4]);
                    TclIO.registerChannel(slaveInterp, chan);
                    break;
                }
            case OPT_TARGET:
                {
                    if (objv.length != 4) {
                        throw new TclNumArgsException(interp, 2, objv, "path alias");
                    }
                    Interp slaveInterp = getInterp(interp, objv[2]);
                    String aliasName = objv[3].toString();
                    Interp targetInterp = InterpAliasCmd.getTargetInterp(slaveInterp, aliasName);
                    if (targetInterp == null) {
                        throw new TclException(interp, "alias \"" + aliasName + "\" in path \"" + objv[2].toString() + "\" not found");
                    }
                    if (!getInterpPath(interp, targetInterp)) {
                        throw new TclException(interp, "target interpreter for alias \"" + aliasName + "\" in path \"" + objv[2].toString() + "\" is not my descendant");
                    }
                    break;
                }
            case OPT_TRANSFER:
                {
                    if (objv.length != 5) {
                        throw new TclNumArgsException(interp, 2, objv, "srcPath channelId destPath");
                    }
                    Interp masterInterp = getInterp(interp, objv[2]);
                    Channel chan = TclIO.getChannel(masterInterp, objv[3].toString());
                    if (chan == null) {
                        throw new TclException(interp, "can not find channel named \"" + objv[3].toString() + "\"");
                    }
                    Interp slaveInterp = getInterp(interp, objv[4]);
                    TclIO.registerChannel(slaveInterp, chan);
                    TclIO.unregisterChannel(masterInterp, chan);
                    break;
                }
        }
    }
