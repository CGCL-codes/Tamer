    private void actionToTransition(Port port, Action action, Var buffer, Var writeIndex, Var readIndex, int bufferSize) {
        ModifyActionScheduler modifyActionScheduler = new ModifyActionScheduler(buffer, writeIndex, port, bufferSize);
        modifyActionScheduler.doSwitch(action.getScheduler());
        modifyActionSchedulability(action, writeIndex, readIndex, OpBinary.GE, factory.createExprInt(numTokens), port);
    }
