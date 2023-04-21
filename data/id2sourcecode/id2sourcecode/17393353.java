    private Action createUntaggedAction(Var readIndex, Var writeIndex, Var storeList, Port port, boolean priority, int bufferSize) {
        Expression expression = factory.createExprBool(true);
        Action newUntaggedAction = createAction(expression, "untagged_" + port.getName());
        Var localINPUT = factory.createVar(0, factory.createTypeList(1, port.getType()), port.getName(), true, 0);
        defineUntaggedBody(readIndex, storeList, newUntaggedAction.getBody(), localINPUT, port, bufferSize);
        modifyActionSchedulability(newUntaggedAction, writeIndex, readIndex, OpBinary.LT, factory.createExprInt(bufferSize), port);
        Pattern pattern = newUntaggedAction.getInputPattern();
        pattern.setNumTokens(port, 1);
        pattern.setVariable(port, localINPUT);
        if (priority) {
            actor.getActionsOutsideFsm().add(0, newUntaggedAction);
        } else {
            actor.getActionsOutsideFsm().add(newUntaggedAction);
        }
        AddedUntaggedActions.add(newUntaggedAction);
        return newUntaggedAction;
    }
