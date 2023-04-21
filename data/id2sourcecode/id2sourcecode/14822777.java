    public void setComponent(ITestComponent c) {
        readWriteLock.writeLock().lock();
        System.out.println("registering component: " + c.toString());
        component = c;
        testComponents();
        readWriteLock.writeLock().unlock();
    }
