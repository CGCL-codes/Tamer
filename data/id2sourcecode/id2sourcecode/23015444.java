    private synchronized RandomAccessFile openMBox(String fileName) throws TemporaryDeliveryException {
        RandomAccessFile target = null;
        FileLock lock = null;
        if (locks.containsKey(fileName)) throw new TemporaryDeliveryException(); else {
            locks.put(fileName, null);
            try {
                target = getRandomAccessFile(fileName);
                lock = getLock(target.getChannel(), fileName);
                if (!lock.isValid()) {
                    throw new TemporaryDeliveryException();
                }
                locks.put(fileName, lock);
            } catch (TemporaryDeliveryException e) {
                locks.remove(fileName);
                throw e;
            }
        }
        return target;
    }
