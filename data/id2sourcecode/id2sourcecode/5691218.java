    protected IProduction removeProductionInternal(IProduction production) {
        ISymbolicProduction symProd = production.getSymbolicProduction();
        Set<String> bufferNames = new HashSet<String>();
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
        }
        _readWriteLock.writeLock().lock();
        String productionName = symProd.getName();
        _allProductionsByName.remove(productionName.toLowerCase());
        _readWriteLock.writeLock().unlock();
        return production;
    }
