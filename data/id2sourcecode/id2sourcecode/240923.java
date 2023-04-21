            @Override
            public void execute() {
                ReentrantReadWriteLock lock = cache.rwl;
                System.out.printf("First release - write = %s, num readers = %d\n", lock.isWriteLocked(), lock.getReadLockCount());
                releaseTo(main);
                System.out.printf("Second release - write = %s, num readers = %d\n", lock.isWriteLocked(), lock.getReadLockCount());
                releaseTo(main);
            }
