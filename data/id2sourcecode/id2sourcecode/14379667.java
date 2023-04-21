    public boolean actionEvaluatePreconditionOriginal() {
        if (envAction == null) {
            throw new InterpreterException("DataflowActorInterpreter: Must call actionSetup() " + "before calling actionEvaluatePrecondition().");
        }
        final Action action = envAction;
        final InputPattern[] inputPatterns = action.getInputPatterns();
        for (int i = 0; i < inputPatterns.length; i++) {
            final InputPattern inputPattern = inputPatterns[i];
            final InputChannel channel = ((InputPort) (inputPortMap.get(inputPattern.getPortname()))).getChannel(0);
            if (inputPattern.getRepeatExpr() == null) {
                if (!channel.hasAvailable(inputPattern.getVariables().length)) {
                    return false;
                }
            } else {
                int repeatVal = configuration.intValue(env.getByName(new EnvironmentKey(inputPattern.getPortname())));
                if (!channel.hasAvailable(inputPattern.getVariables().length * repeatVal)) {
                    return false;
                }
            }
        }
        final Expression[] guards = action.getGuards();
        for (int i = 0; i < guards.length; i++) {
            final Object g = interpreter.valueOf(guards[i], env);
            if (!configuration.booleanValue(g)) {
                return false;
            }
        }
        return true;
    }
