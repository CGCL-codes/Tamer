    private void modifyNoRepeatActionsInFSM() {
        for (Transition transition : fsm.getTransitions()) {
            Action action = transition.getAction();
            if (noRepeatActions.contains(action)) {
                ListIterator<Port> it = action.getInputPattern().getPorts().listIterator();
                while (it.hasNext()) {
                    Port port = it.next();
                    if (inputPorts.contains(port)) {
                        int position = portPosition(inputPorts, port);
                        Procedure body = action.getBody();
                        Var buffer = inputBuffers.get(position);
                        Var writeIndex = writeIndexes.get(position);
                        Var readIndex = readIndexes.get(position);
                        int bufferSize = bufferSizes.get(position);
                        ModifyActionScheduler modifyActionScheduler = new ModifyActionScheduler(buffer, writeIndex, port, bufferSize);
                        modifyActionScheduler.doSwitch(action.getBody());
                        modifyActionScheduler.doSwitch(action.getScheduler());
                        modifyActionSchedulability(action, writeIndex, readIndex, OpBinary.NE, factory.createExprInt(0), port);
                        consumeToken(body, position, port);
                        noRepeatActions.remove(action);
                        int index = it.previousIndex();
                        action.getInputPattern().remove(port);
                        it = action.getInputPattern().getPorts().listIterator(index);
                    }
                }
            }
        }
    }
