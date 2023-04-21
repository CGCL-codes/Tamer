    synchronized void printErrorStatus(String header) {
        if (numReaders > 0 && writeLockHeld) {
            System.out.println("Invalid state after " + header + "\n\tReaders Waiting:\t" + readersWaiting + "\n\tActive Readers:\t" + numReaders + "\n\tWriters Waiting:\t" + writersWaiting + "\n\tWrite Lock Held:\t" + writeLockHeld);
            System.out.flush();
        }
    }
