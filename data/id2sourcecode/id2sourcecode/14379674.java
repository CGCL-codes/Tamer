    public void actionComputeOutputs() {
        if (envAction == null) {
            throw new InterpreterException("DataflowActorInterpreter: Must call actionSetup() " + "before calling actionComputeOutputs().");
        }
        final Action action = envAction;
        final OutputExpression[] outputExpressions = action.getOutputExpressions();
        for (int i = 0; i < outputExpressions.length; i++) {
            final OutputExpression outputExpression = outputExpressions[i];
            final Expression[] expressions = outputExpression.getExpressions();
            final Expression repeatExpr = outputExpression.getRepeatExpr();
            final OutputChannel channel = ((OutputPort) (outputPortMap.get(outputExpression.getPortname()))).getChannel(0);
            if (repeatExpr != null) {
                int repeatValue = configuration.intValue(interpreter.valueOf(repeatExpr, env));
                List[] lists = new List[expressions.length];
                for (int j = 0; j < lists.length; j++) {
                    lists[j] = configuration.getList(interpreter.valueOf(expressions[j], env));
                }
                for (int j = 0; j < repeatValue; j++) {
                    for (int k = 0; k < expressions.length; k++) {
                        channel.put(lists[k].get(j));
                    }
                }
            } else {
                for (int j = 0; j < expressions.length; j++) {
                    channel.put(interpreter.valueOf(expressions[j], env));
                }
            }
        }
    }
