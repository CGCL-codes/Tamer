    @Override
    public void dispose() {
        super.dispose();
        try {
            _readWriteLock.writeLock().lock();
            for (IProduction production : _allProductionsByName.values()) production.dispose();
            _allProductionsByName.clear();
            _allProductionsByName = null;
            _eventDispatcher.clear();
            _eventDispatcher = null;
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }
