    DependencyStatus accept(LockType locktype) {
        LOGGER.debug("Checking lock %s for resource %s (generated %b)", locktype, this, getState());
        if (state == ResourceState.ERROR) return DependencyStatus.ERROR;
        if (state == ResourceState.WAITING) return DependencyStatus.WAIT;
        switch(locktype) {
            case GENERATED:
                return getState() == ResourceState.DONE ? DependencyStatus.OK : DependencyStatus.WAIT;
            case EXCLUSIVE_ACCESS:
                return writers == 0 && readers == 0 ? DependencyStatus.OK_LOCK : DependencyStatus.WAIT;
            case READ_ACCESS:
                switch(lockmode) {
                    case EXCLUSIVE_WRITER:
                    case SINGLE_WRITER:
                        return writers == 0 ? DependencyStatus.OK : DependencyStatus.WAIT;
                    case MULTIPLE_WRITER:
                        return DependencyStatus.OK;
                    case READ_ONLY:
                        return getState() == ResourceState.DONE ? DependencyStatus.OK : DependencyStatus.WAIT;
                }
                break;
            case WRITE_ACCESS:
                switch(lockmode) {
                    case EXCLUSIVE_WRITER:
                        return writers == 0 && readers == 0 ? DependencyStatus.OK_LOCK : DependencyStatus.WAIT;
                    case MULTIPLE_WRITER:
                        return DependencyStatus.OK;
                    case READ_ONLY:
                        return DependencyStatus.ERROR;
                    case SINGLE_WRITER:
                        return writers == 0 ? DependencyStatus.OK_LOCK : DependencyStatus.WAIT;
                }
        }
        return DependencyStatus.ERROR;
    }
