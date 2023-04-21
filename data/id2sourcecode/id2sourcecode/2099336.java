    public void preventMultipleInstances() throws MultipleInstancesException {
        try {
            FileOutputStream fos = new FileOutputStream(lockFile);
            multipleInstanceLock = fos.getChannel().tryLock();
        } catch (Exception e) {
            final String message = "Multiple instances must be running because we got an exception trying to acquire a lock on " + lockFile.toString();
            System.err.println(message);
            e.printStackTrace();
            throw new MultipleInstancesException(message, e);
        }
        if (multipleInstanceLock == null) {
            final String message = "Multiple instances must be running because I could not acquire a lock on " + lockFile.toString();
            System.err.println(message);
            throw new MultipleInstancesException(message);
        }
    }
