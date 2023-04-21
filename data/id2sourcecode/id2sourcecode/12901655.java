                @Override
                public LockType getLockType() {
                    String lockType = readText(SimplemodelParser.LOCK_TYPE);
                    if ("reentrant".equals(lockType)) {
                        return LockType.Reentrant;
                    }
                    if ("synchronized".equals(lockType)) {
                        return LockType.Synchronized;
                    }
                    if ("readwrite".equals(lockType)) {
                        return LockType.ReentrantReadWrite;
                    }
                    return LockType.None;
                }
