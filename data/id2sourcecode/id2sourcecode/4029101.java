    public String toString() {
        StringBuffer s = new StringBuffer(super.toString());
        s.append(": readLocks = ").append(readLocks).append(", writeLocks = ").append(writeLocks).append(", writeLockedBy = ").append(writeLockedBy);
        return s.toString();
    }
