    private void runWithInstance(InstanceCredType instanceCred, InstanceRunnable runable, boolean write) throws EngineException {
        long instanceID = instanceCred.getId();
        Lock lock = null;
        if (true) {
            instanceLocks.putIfAbsent(instanceID, new ReentrantReadWriteLock());
            ReentrantReadWriteLock rwlock = instanceLocks.get(instanceID);
            lock = write ? rwlock.writeLock() : rwlock.readLock();
        } else {
        }
        lock.lock();
        EntityManager em = createEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            Instance instance = em.find(Instance.class, instanceID);
            if (instance == null) {
                instanceLocks.remove(instanceID);
                throw new EngineException("invalid instanceID=" + instanceID);
            }
            instance.setEngine(this);
            if (!instance.getKey().equals(instanceCred.getKey())) {
                throw new EngineException("Illegal key for instance with ID=" + instanceID);
            }
            runable.instance = instance;
            runable.em = em;
            runable.run();
            if (tx.isActive()) tx.commit();
        } catch (RuntimeException re) {
            throw new RuntimeException("Failed during ??? on instanceID=" + instanceID, re);
        } finally {
            if (tx != null && tx.isActive()) tx.rollback();
            em.close();
            lock.unlock();
        }
    }
