    private void scanUntaggedInputs(Action action) {
        for (Entry<Port, Integer> verifEntry : action.getInputPattern().getNumTokensMap().entrySet()) {
            int verifNumTokens = verifEntry.getValue();
            Port verifPort = verifEntry.getKey();
            Type entryType = verifPort.getType();
            int bufferSize = OptimalBufferSize(verifPort);
            if (inputPorts.contains(verifPort)) {
                int position = portPosition(inputPorts, verifPort);
                Var buffer = inputBuffers.get(position);
                Var writeIndex = writeIndexes.get(position);
                Var readIndex = readIndexes.get(position);
                bufferSize = bufferSizes.get(position);
                ModifyActionScheduler modifyActionScheduler = new ModifyActionScheduler(buffer, writeIndex, verifPort, bufferSize);
                modifyActionScheduler.doSwitch(action.getBody());
                modifyActionScheduler.doSwitch(action.getScheduler());
                modifyActionSchedulability(action, writeIndex, readIndex, OpBinary.GE, factory.createExprInt(verifNumTokens), verifPort);
                updateUntagIndex(action, writeIndex, verifNumTokens);
                action.getInputPattern().remove(verifPort);
            } else {
                if (verifNumTokens > 1) {
                    Var untagBuffer = factory.createVar(0, entryType, "buffer", true, true);
                    Var untagReadIndex = factory.createVar(0, factory.createTypeInt(32), "UntagReadIndex", true, factory.createExprInt(0));
                    Var untagWriteIndex = factory.createVar(0, factory.createTypeInt(32), "UntagWriteIndex", true, factory.createExprInt(0));
                    inputPorts.add(verifPort);
                    untagBuffer = createTab(verifPort.getName() + "_buffer", entryType, bufferSize);
                    inputBuffers.add(untagBuffer);
                    untagReadIndex = createCounter("readIndex_" + verifPort.getName());
                    readIndexes.add(untagReadIndex);
                    untagWriteIndex = createCounter("writeIndex_" + verifPort.getName());
                    writeIndexes.add(untagWriteIndex);
                    bufferSizes.add(bufferSize);
                    createUntaggedAction(untagReadIndex, untagWriteIndex, untagBuffer, verifPort, false, bufferSize);
                    ModifyActionScheduler modifyActionScheduler = new ModifyActionScheduler(untagBuffer, untagWriteIndex, verifPort, bufferSize);
                    modifyActionScheduler.doSwitch(action.getBody());
                    modifyActionScheduler.doSwitch(action.getScheduler());
                    modifyActionSchedulability(action, untagWriteIndex, untagReadIndex, OpBinary.GE, factory.createExprInt(verifNumTokens), verifPort);
                    updateUntagIndex(action, untagWriteIndex, verifNumTokens);
                    action.getInputPattern().remove(verifPort);
                }
            }
        }
        AddedUntaggedActions.add(action);
    }
