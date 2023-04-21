        public final Value doApply(Interpreter r, Value[] vlr) throws ContinuationException {
            final int vls = vlr.length;
            SIZESWITCH: switch(vls) {
                case 0:
                    switch(id) {
                        case CLASSPATHEXTENSION:
                            URL[] urls = r.dynenv.getClassPath();
                            Pair p = EMPTYLIST;
                            for (int i = urls.length - 1; i >= 0; i--) {
                                p = new Pair(new SchemeString(urls[i].toString()), p);
                            }
                            return p;
                        case COMPACTSTRINGREP:
                            return truth(SchemeString.compactRepresentation);
                        case CURRENTWIND:
                            return r.dynenv.wind;
                        case GENSYM:
                            long unv = r.tctx.nextUnique();
                            return Symbol.intern(GENSYM_MAGIC_PREFIX + base64encode(unv));
                        case INTERACTIONENVIRONMENT:
                            return r.tpl.asValue();
                        case SISCINITIAL:
                            try {
                                return new MemorySymEnv(r.lookupContextEnv(Util.SISC_SPECIFIC));
                            } catch (ArrayIndexOutOfBoundsException e) {
                                throwPrimException(liMessage(SISCB, "nosiscspecificenv"));
                            }
                        default:
                            break SIZESWITCH;
                    }
                case 1:
                    switch(id) {
                        case CLASSPATHEXTENSIONAPPEND:
                            for (Pair p = pair(vlr[0]); p != EMPTYLIST; p = (Pair) p.cdr()) {
                                r.dynenv.extendClassPath(url(p.car()));
                            }
                            return VOID;
                        case SEALIMMUTABLEPAIR:
                            immutablePair(vlr[0]).makeImmutable();
                            return VOID;
                        case SEALIMMUTABLEVECTOR:
                            immutableVector(vlr[0]).makeImmutable();
                            return VOID;
                        case COMPACTSTRINGREP:
                            SchemeString.compactRepresentation = truth(vlr[0]);
                            return VOID;
                        case NUMBER2STRING:
                            return new SchemeString(num(vlr[0]).toString());
                        case GETENVIRONMENT:
                            try {
                                return r.getCtx().lookupContextEnv(symbol(vlr[0])).asValue();
                            } catch (ArrayIndexOutOfBoundsException e) {
                                throwPrimException(liMessage(SISCB, "noenv", vlr[0].synopsis()));
                                return VOID;
                            }
                        case PARENTENVIRONMENT:
                            SymbolicEnvironment env = env(vlr[0]);
                            SymbolicEnvironment parent = env.getParent();
                            if (parent == null) return FALSE; else return parent.asValue();
                        case GETSIDECAR:
                            return r.tpl.getSidecarEnvironment(symbol(vlr[0])).asValue();
                        case GETENV:
                            String str = r.getCtx().getProperty(string(vlr[0]));
                            if (str == null) {
                                return FALSE;
                            } else {
                                return new SchemeString(str);
                            }
                        case GENSYM:
                            long unv = r.tctx.nextUnique();
                            return Symbol.intern(GENSYM_MAGIC_PREFIX + base64encode(unv));
                        case COMPILE:
                            return new Closure(false, (short) 0, r.compile(vlr[0]), ZV, new int[0]);
                        case CALLEC:
                            Value kproc = vlr[0];
                            r.setupTailCall(CALLEC_APPEVAL, r.captureEscapingContinuation());
                            return kproc;
                        case CALLCC:
                            kproc = vlr[0];
                            r.setupTailCall(CALLCC_APPEVAL, r.captureContinuation());
                            return kproc;
                        case CALLFC:
                            kproc = vlr[0];
                            r.setupTailCall(CALLFC_APPEVAL, r.fk.capture(r));
                            return kproc;
                        case CURRENTWIND:
                            r.dynenv.wind = vlr[0];
                            return VOID;
                        case LOADNL:
                            try {
                                Class clazz = Class.forName(string(vlr[0]), true, Util.currentClassLoader());
                                return (NativeLibrary) clazz.newInstance();
                            } catch (Exception e) {
                                throwPrimException(e.getMessage());
                            }
                        case GETPROP:
                            int loc = r.tpl.getLoc(symbol(vlr[0]));
                            if (loc == -1) return FALSE; else return r.tpl.lookup(loc);
                        case STRING2NUMBER:
                            String st = string(vlr[0]);
                            try {
                                return (Quantity) r.dynenv.parser.nextExpression(new PushbackReader(new StringReader(st)));
                            } catch (ClassCastException cce) {
                                return FALSE;
                            } catch (NumberFormatException nf) {
                                return FALSE;
                            } catch (IOException e) {
                                return FALSE;
                            }
                        case NLBINDINGNAMES:
                            Value[] va = nlib(vlr[0]).getLibraryBindingNames(r);
                            return valArrayToList(va, 0, va.length);
                        case INTERACTIONENVIRONMENT:
                            Value last = r.getCtx().toplevel_env.asValue();
                            r.getCtx().toplevel_env = env(vlr[0]);
                            return last;
                        case REPORTENVIRONMENT:
                            if (FIVE.equals(num(vlr[0]))) try {
                                return new MemorySymEnv(r.lookupContextEnv(Util.REPORT));
                            } catch (ArrayIndexOutOfBoundsException e) {
                                throwPrimException(liMessage(SISCB, "noreportenv"));
                            } else throwPrimException(liMessage(SISCB, "unsupportedstandardver"));
                        case NULLENVIRONMENT:
                            switch(num(vlr[0]).indexValue()) {
                                case 5:
                                    MemorySymEnv ae = new MemorySymEnv();
                                    sisc.compiler.Compiler.addSpecialForms(ae);
                                    return ae;
                                case 0:
                                    return new MemorySymEnv();
                                default:
                                    throwPrimException(liMessage(SISCB, "unsupportedstandardver"));
                                    return VOID;
                            }
                        default:
                            break SIZESWITCH;
                    }
                case 2:
                    switch(id) {
                        case NLBINDING:
                            return nlib(vlr[0]).getBindingValue(r, symbol(vlr[1]));
                        case COMPILE:
                            return new Closure(false, (short) 0, r.compile(vlr[0], env(vlr[1])), ZV, new int[0]);
                        case WITHENVIRONMENT:
                            Procedure thunk = proc(vlr[1]);
                            r.tpl = env(vlr[0]);
                            r.setupTailCall(WITHENV_APPEVAL, ZV);
                            return thunk;
                        case WITHFC:
                            Procedure proc = proc(vlr[1]);
                            Procedure ehandler = proc(vlr[0]);
                            r.setFailureContinuation(new ApplyValuesContEval(ehandler));
                            r.setupTailCall(WITHFC_APPEVAL, ZV);
                            return proc;
                        case CALLWITHVALUES:
                            Procedure producer = proc(vlr[0]);
                            Procedure consumer = proc(vlr[1]);
                            r.pushExpr(new ApplyValuesContEval(consumer));
                            r.setupTailCall(CALLWITHVALUES_APPEVAL, ZV);
                            return producer;
                        case GETPROP:
                            Value ret = null;
                            if (vlr[1] instanceof SymbolicEnvironment) {
                                ret = env(vlr[1]).lookup(symbol(vlr[0]));
                            } else {
                                ret = r.tpl.getSidecarEnvironment(symbol(vlr[1])).lookup(symbol(vlr[0]));
                            }
                            return (ret == null) ? FALSE : ret;
                        case REMPROP:
                            if (vlr[1] instanceof SymbolicEnvironment) {
                                env(vlr[1]).undefine(symbol(vlr[0]));
                            } else {
                                r.tpl.getSidecarEnvironment(symbol(vlr[1])).undefine(symbol(vlr[0]));
                            }
                            return VOID;
                        case PUTPROP:
                            r.tpl.define(symbol(vlr[0]), vlr[1]);
                            return VOID;
                        case SETBOX:
                            try {
                                box(vlr[0]).set(vlr[1]);
                            } catch (ImmutableException e) {
                                throwPrimException(liMessage(SISCB, "isimmutable", "box", vlr[0].synopsis()));
                            }
                            return VOID;
                        case SETCAR:
                            truePair(vlr[0]).setCar(vlr[1]);
                            return VOID;
                        case SETCDR:
                            truePair(vlr[0]).setCdr(vlr[1]);
                            return VOID;
                        case SETENVIRONMENT:
                            r.getCtx().defineContextEnv(symbol(vlr[0]), env(vlr[1]));
                            return VOID;
                        case SIGHOOK:
                            SignalHook.addHandler(string(vlr[0]), proc(vlr[1]), r.dynenv);
                            return VOID;
                        case SIGUNHOOK:
                            SignalHook.removeHandler(string(vlr[0]), proc(vlr[1]), r.dynenv);
                            return VOID;
                        case GETSIDECAR:
                            return env(vlr[1]).getSidecarEnvironment(symbol(vlr[0])).asValue();
                        case STRING2NUMBER:
                            try {
                                int radix = num(vlr[1]).indexValue();
                                if (r.dynenv.parser.lexer.strictR5RS && !(radix == 10 || radix == 16 || radix == 2 || radix == 8)) throwPrimException(liMessage(SISCB, "invalidradix"));
                                return (Quantity) r.dynenv.parser.nextExpression(new PushbackReader(new StringReader(string(vlr[0]))), radix, 0);
                            } catch (NumberFormatException nf) {
                                return FALSE;
                            } catch (IOException e) {
                                return FALSE;
                            }
                        case NUMBER2STRING:
                            int radix = num(vlr[1]).indexValue();
                            if (r.dynenv.parser.lexer.strictR5RS && !(radix == 10 || radix == 16 || radix == 2 || radix == 8)) throwPrimException(liMessage(SISCB, "invalidradix"));
                            return new SchemeString(num(vlr[0]).toString(radix));
                        case STRINGFILL:
                            SchemeString st = str(vlr[0]);
                            char c = character(vlr[1]);
                            for (int i = 0; i < st.length(); i++) st.set(i, c);
                            return VOID;
                        case VECTORFILL:
                            vec(vlr[0]).fill(vlr[1]);
                            return VOID;
                        default:
                            break SIZESWITCH;
                    }
                case 3:
                    switch(id) {
                        case STRINGSET:
                            int index = num(vlr[1]).indexValue();
                            try {
                                str(vlr[0]).set(index, character(vlr[2]));
                            } catch (ArrayIndexOutOfBoundsException e) {
                                throwPrimException(liMessage(SISCB, "indexoob", new Object[] { new Integer(index), vlr[0].synopsis() }));
                            }
                            return VOID;
                        case VECTORSET:
                            index = num(vlr[1]).indexValue();
                            try {
                                vec(vlr[0]).set(index, vlr[2]);
                            } catch (ArrayIndexOutOfBoundsException e) {
                                throwPrimException(liMessage(SISCB, "indexoob", new Object[] { new Integer(index), vlr[0].synopsis() }));
                            }
                            return VOID;
                        case GETPROP:
                            Value ret = null;
                            if (vlr[1] instanceof SymbolicEnvironment) {
                                ret = env(vlr[1]).lookup(symbol(vlr[0]));
                            } else {
                                ret = r.tpl.getSidecarEnvironment(symbol(vlr[1])).lookup(symbol(vlr[0]));
                            }
                            return (ret == null) ? vlr[2] : ret;
                        case PUTPROP:
                            Symbol lhs = symbol(vlr[0]);
                            Value rhs = vlr[2];
                            SymbolicEnvironment env;
                            if (vlr[1] instanceof SymbolicEnvironment) {
                                env = (SymbolicEnvironment) vlr[1];
                            } else {
                                env = r.tpl.getSidecarEnvironment((Symbol) vlr[1]);
                            }
                            updateName(rhs, lhs);
                            env.define(lhs, rhs);
                            return VOID;
                    }
            }
            switch(id) {
                case APPLY:
                    Procedure proc = proc(vlr[0]);
                    int l = vls - 2;
                    Pair args = pair(vlr[l + 1]);
                    Value newvlr[] = r.createValues(l + length(args));
                    int j;
                    for (j = 0; j < l; j++) {
                        newvlr[j] = vlr[j + 1];
                    }
                    for (; args != EMPTYLIST; args = (Pair) args.cdr()) {
                        newvlr[j++] = args.car();
                    }
                    r.setupTailCall(APPLY_APPEVAL, newvlr);
                    return proc;
                default:
                    throwArgSizeException();
            }
            return VOID;
        }
