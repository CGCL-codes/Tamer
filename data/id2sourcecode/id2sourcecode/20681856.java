    public static void main(final String[] args) {
        final ReadWriteLock aRWLock = new ReentrantReadWriteLock();
        aRWLock.readLock().lock();
        try {
            System.out.println("in readLock");
        } finally {
            aRWLock.readLock().unlock();
        }
        aRWLock.writeLock().lock();
        try {
            System.out.println("in writeLock");
        } finally {
            aRWLock.writeLock().unlock();
        }
        aRWLock.readLock().lock();
        try {
            aRWLock.readLock().lock();
            try {
                System.out.println("in double readLock");
            } finally {
                aRWLock.readLock().unlock();
            }
        } finally {
            aRWLock.readLock().unlock();
        }
        aRWLock.writeLock().lock();
        try {
            aRWLock.writeLock().lock();
            try {
                System.out.println("in double writeLock");
            } finally {
                aRWLock.writeLock().unlock();
            }
        } finally {
            aRWLock.writeLock().unlock();
        }
        if (false) {
            aRWLock.readLock().lock();
            try {
                aRWLock.writeLock().lock();
                try {
                    System.out.println("in readLock and writeLock");
                } finally {
                    aRWLock.writeLock().unlock();
                }
            } finally {
                aRWLock.readLock().unlock();
            }
        }
        aRWLock.writeLock().lock();
        try {
            aRWLock.readLock().lock();
            try {
                System.out.println("in writeLock and readLock");
            } finally {
                aRWLock.readLock().unlock();
            }
        } finally {
            aRWLock.writeLock().unlock();
        }
        System.out.println("-done-");
    }
