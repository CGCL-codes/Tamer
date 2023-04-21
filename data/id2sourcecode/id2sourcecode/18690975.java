    protected synchronized void checkLock() throws RegistryException {
        if (lockOwner != Thread.currentThread()) throw new RegistryException("Attempt to modify UpdatableResource <" + this + "> without owning the exclusive write lock.");
    }
