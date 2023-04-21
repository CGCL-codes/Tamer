        @Override
        protected UnitOfWork initialValue() {
            log.debug("create UnitOfWork");
            UnitOfWork uow = new UnitOfWork(cd(), readLock, writeLock);
            return uow;
        }
