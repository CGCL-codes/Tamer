    public void perform(CommandContext context, CommandArguments arguments) throws CommandException, MissingArgumentsException {
        PrintWriter writer = context.getWriter();
        DebuggingContext dc = context.getDebuggingContext();
        ThreadReference thread = dc.getThread();
        int frame = dc.getFrame();
        int start = 0;
        try {
            start = Integer.parseInt(arguments.peek());
            arguments.nextToken();
            if (!arguments.hasMoreTokens()) {
                throw new MissingArgumentsException();
            }
        } catch (NumberFormatException nfe) {
        }
        int end = -1;
        try {
            end = Integer.parseInt(arguments.peek());
            arguments.nextToken();
            if (!arguments.hasMoreTokens()) {
                throw new MissingArgumentsException();
            }
        } catch (NumberFormatException nfe) {
        }
        arguments.returnAsIs(true);
        String expr = arguments.rest();
        Evaluator eval = new Evaluator(expr);
        Object o = null;
        try {
            o = eval.evaluate(thread, frame);
        } catch (EvaluationException ee) {
            throw new CommandException(NbBundle.getMessage(getClass(), "ERR_EvaluationError", ee.getMessage()));
        }
        if (o instanceof ArrayReference) {
            try {
                writer.println(printArray((ArrayReference) o, start, end, thread));
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
                    writer.println(printCollection(or, start, end, thread));
                } catch (Exception e) {
                    throw new CommandException(e.toString(), e);
                }
            } else if (isaMap) {
                if (start > 0 || end >= 0) {
                    throw new CommandException(NbBundle.getMessage(getClass(), "ERR_elements_MapNoIndex"));
                }
                try {
                    writer.println(printMap(or, thread));
                } catch (Exception e) {
                    throw new CommandException(e.toString(), e);
                }
            } else {
                throw new CommandException(NbBundle.getMessage(getClass(), "ERR_elements_NotCollection"));
            }
        } else if (o == null) {
            writer.println(NbBundle.getMessage(getClass(), "ERR_elements_IsNull"));
        } else {
            throw new CommandException(NbBundle.getMessage(getClass(), "ERR_elements_NotCollection"));
        }
    }
