    private boolean preconfigureScriptNode(IScriptNode node) {
        boolean success = true;
        try {
            SubstitutionFailureLevel inheritedSubstLevel = getInheritedSubstitutionFailureLevel();
            node.setEffectiveSubstitutionFailureLevel(inheritedSubstLevel);
            IChannelScope previousChannelScope = (callStack.isEmpty()) ? null : peek().getChannelScope();
            String scope = node.getDeclaredScope();
            if (scope != null) {
                node.setStartsNewChannelScope(true);
                node.setChannelScope(new ChannelScope(node.getPathToNode(), previousChannelScope));
            } else if (previousChannelScope != null) {
                node.setStartsNewChannelScope(false);
                node.setChannelScope(previousChannelScope);
            } else {
                node.setStartsNewChannelScope(true);
                node.setChannelScope(new ChannelScope("BizViewSession", null));
            }
            ITransactionContext previousTxContext = (callStack.isEmpty()) ? null : peek().getTransactionContext();
            node.setDeclaredTransactionPropagation();
            TransactionPropagation declaredTxPropagationLevel = node.getDeclaredTransactionPropagation();
            ITransactionContext newTxContext = TransactionContext.startNewTransactionIfNeeded(declaredTxPropagationLevel, previousTxContext, node.getPathToNode());
            node.setStartsNewTransactionScope(newTxContext != previousTxContext);
            node.setTransactionContext(newTxContext);
        } catch (Exception e) {
            parkException(e);
        }
        try {
            node.setPathToErrorHandler();
        } catch (Exception e) {
            parkException(e);
            success = false;
        }
        return success;
    }
