    protected IProduction addProductionInternal(IProduction production) {
        ISymbolicProduction symProd = production.getSymbolicProduction();
        Set<String> bufferNames = new HashSet<String>();
        Set<String> ambiguousBufferNames = new HashSet<String>();
        for (ICondition condition : symProd.getConditions()) if (condition instanceof ChunkTypeCondition) {
            ChunkTypeCondition ctc = (ChunkTypeCondition) condition;
            ctc.getChunkType();
            bufferNames.add(ctc.getBufferName());
        } else if (condition instanceof ChunkCondition) {
            ChunkCondition cc = (ChunkCondition) condition;
            cc.getChunk().getSymbolicChunk().getChunkType();
            bufferNames.add(cc.getBufferName());
        } else if (condition instanceof AbstractBufferCondition) {
            String bufferName = ((AbstractBufferCondition) condition).getBufferName();
            if (condition instanceof VariableCondition) bufferNames.add(bufferName);
            ambiguousBufferNames.add(bufferName);
        }
        Collection<IAction> actions = new ArrayList<IAction>(symProd.getActions());
        IModel model = getModel();
        for (String bufferName : bufferNames) {
            IActivationBuffer buffer = model.getActivationBuffer(bufferName);
            boolean actionFound = false;
            if (buffer.isStrictHarvestingEnabled()) {
                if (LOGGER.isDebugEnabled()) LOGGER.debug("Strict harvesting enabled for " + bufferName + ", checking actions of " + symProd.getName());
                for (IAction action : actions) if (action instanceof IBufferAction && ((IBufferAction) action).getBufferName().equals(bufferName)) {
                    if (action instanceof ModifyAction && !(action instanceof RemoveAction) && ((ModifyAction) action).getSlots().size() == 0) symProd.removeAction(action);
                    actionFound = true;
                    break;
                }
                if (!actionFound) {
                    if (LOGGER.isDebugEnabled()) LOGGER.debug(bufferName + " requires strict harvest but " + symProd.getName() + " doesn't operate on the buffer after the match. Adding a remove");
                    symProd.addAction(new RemoveAction(bufferName));
                }
            }
        }
        _readWriteLock.writeLock().lock();
        String productionName = getSafeName(symProd.getName(), _allProductionsByName);
        symProd.setName(productionName);
        production.encode();
        _allProductionsByName.put(productionName.toLowerCase(), production);
        _readWriteLock.writeLock().unlock();
        fireProductionAdded(production);
        return production;
    }
