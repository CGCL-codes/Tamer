    public String toString() {
        return r + "\nopen: " + openCalls + " close: " + closeCalls + " read: " + readCalls + " write: " + writeCalls + " number of counterresets: " + resetCount + " sequentialAccess: " + sequentialAccessCount + " randomAccess: " + randomAccessCount + " sameAccess: " + sameSectorCount + "\n" + "Sector distance histogram:\n" + histSectorDistance + "Time histogram:\n" + histTime;
    }
