    public void perform(Session session, CommandArguments args, Log out) {
        if (!session.isActive()) {
            throw new CommandException(Bundle.getString("noActiveSession"));
        }
        ContextManager ctxtman = (ContextManager) session.getManager(ContextManager.class);
        ThreadReference thread = ctxtman.getCurrentThread();
        if (thread == null) {
            throw new CommandException(Bundle.getString("noCurrentThread"));
        }
        int frame = ctxtman.getCurrentFrame();
        if (!args.hasMoreTokens()) {
            throw new MissingArgumentsException();
        }
        Preferences prefs = Preferences.userRoot().node("com/bluemarsh/jswat/util");
        int timeout = prefs.getInt("invocationTimeout", Defaults.INVOCATION_TIMEOUT);
        int start = 0;
        try {
            start = Integer.parseInt(args.peek());
            args.nextToken();
            if (!args.hasMoreTokens()) {
                throw new MissingArgumentsException();
            }
        } catch (NumberFormatException nfe) {
        }
        int end = -1;
        try {
            end = Integer.parseInt(args.peek());
            args.nextToken();
            if (!args.hasMoreTokens()) {
                throw new MissingArgumentsException();
            }
        } catch (NumberFormatException nfe) {
        }
        args.returnAsIs(true);
        String expr = args.rest();
        Evaluator eval = new Evaluator(expr);
        Object o = null;
        try {
            o = eval.evaluate(thread, frame);
        } catch (EvaluationException ee) {
            throw new CommandException(Bundle.getString("evalError") + ' ' + ee.getMessage());
        }
        if (o instanceof ArrayReference) {
            try {
                out.writeln(printArray((ArrayReference) o, start, end, thread));
            } catch (Exception e) {
                throw new CommandException(e.toString(), e);
            }
        } else if (o instanceof ObjectReference) {
            boolean isaCollection = false;
            boolean isaMap = false;
            ObjectReference or = (ObjectReference) o;
            ReferenceType rt = or.referenceType();
            if (rt instanceof ClassType) {
                ClassType ct = (ClassType) rt;
                List interfaces = ct.allInterfaces();
                if (interfaces.size() > 0) {
                    Iterator iter = interfaces.iterator();
                    while (iter.hasNext()) {
                        ReferenceType intf = (ReferenceType) iter.next();
                        String name = intf.name();
                        if (name.equals("java.util.Collection")) {
                            isaCollection = true;
                            break;
                        } else if (name.equals("java.util.Map")) {
                            isaMap = true;
                            break;
                        }
                    }
                }
            }
            if (isaCollection) {
                try {
                    out.writeln(printCollection(or, start, end, thread, timeout));
                } catch (Exception e) {
                    throw new CommandException(e.toString(), e);
                }
            } else if (isaMap) {
                if (start > 0 || end >= 0) {
                    throw new CommandException(Bundle.getString("elements.mapNoIndex"));
                }
                try {
                    out.writeln(printMap(or, thread, timeout));
                } catch (Exception e) {
                    throw new CommandException(e.toString(), e);
                }
            } else {
                throw new CommandException(Bundle.getString("elements.whatIsIt"));
            }
        } else if (o == null) {
            out.writeln(Bundle.getString("elements.isNull"));
        } else {
            throw new CommandException(Bundle.getString("elements.whatIsIt"));
        }
    }
