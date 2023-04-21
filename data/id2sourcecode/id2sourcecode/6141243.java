    DependencyStatus accept(LockType locktype) {
        LOGGER.info("Checking lock %s for resource %s (generated %b)", locktype, this, generated);
        switch(locktype) {
            case GENERATED:
                return generated ? DependencyStatus.GO : DependencyStatus.WAIT;
            case EXCLUSIVE_ACCESS:
                return writers == 0 && readers == 0 ? DependencyStatus.OK_LOCK : DependencyStatus.WAIT;
            case READ_ACCESS:
                switch(lockmode) {
                    case EXCLUSIVE_WRITER:
                    case SINGLE_WRITER:
                        return writers == 0 ? DependencyStatus.OK_STATUS : DependencyStatus.WAIT;
                    case MULTIPLE_WRITER:
                        return DependencyStatus.GO;
                    case READ_ONLY:
                        return generated ? DependencyStatus.GO : DependencyStatus.WAIT;
                }
                break;
            case WRITE_ACCESS:
                switch(lockmode) {
                    case EXCLUSIVE_WRITER:
                        return writers == 0 && readers == 0 ? DependencyStatus.OK_LOCK : DependencyStatus.WAIT;
                    case MULTIPLE_WRITER:
                        return DependencyStatus.GO;
                    case READ_ONLY:
                        return DependencyStatus.ERROR;
                    case SINGLE_WRITER:
                        return writers == 0 ? DependencyStatus.OK_LOCK : DependencyStatus.WAIT;
                }
        }
        return DependencyStatus.ERROR;
    }
