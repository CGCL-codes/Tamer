    public void actionSetup(Action action) {
        assert envAction == null;
        env = null;
        envAction = null;
        final DynamicEnvironmentFrame local = new DynamicEnvironmentFrame(actorEnv);
        Executor myInterpreter = new Executor(theConfiguration, this.actorEnv);
        TypeSystem ts = theConfiguration.getTypeSystem();
        final InputPattern[] inputPatterns = action.getInputPatterns();
        for (int i = 0; i < inputPatterns.length; i++) {
            final InputPattern inputPattern = inputPatterns[i];
            final String[] vars = inputPattern.getVariables();
            final Expression repExpr = inputPattern.getRepeatExpr();
            if (repExpr == null) {
                for (int j = 0; j < vars.length; j++) {
                    final InputChannel channel = ((InputPort) (inputPortMap.get(inputPattern.getPortname()))).getChannel(0);
                    local.bind(vars[j], new SingleTokenReaderThunk(channel, j), null);
                }
            } else {
                Thunk repExprThunk = new Thunk(repExpr, interpreter, local);
                local.bind(new EnvironmentKey(inputPattern.getPortname()), repExprThunk, null);
                for (int j = 0; j < vars.length; j++) {
                    final InputChannel channel = ((InputPort) (inputPortMap.get(inputPattern.getPortname()))).getChannel(0);
                    local.bind(vars[j], new MultipleTokenReaderThunk(channel, j, vars.length, repExprThunk, configuration), null);
                }
            }
        }
        final Decl[] decls = action.getDecls();
        for (int i = 0; i < decls.length; i++) {
            final Expression v = decls[i].getInitialValue();
            TypeExpr te = decls[i].getType();
            Type type = (ts != null) ? ts.evaluate(te, myInterpreter) : null;
            if (v == null) {
                local.bind(decls[i].getName(), null, type);
            } else {
                local.bind(decls[i].getName(), new Thunk(v, interpreter, local, type), type);
            }
        }
        env = local;
        envAction = action;
    }
