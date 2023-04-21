        synchronized int getWriteHoldCount() {
            return isWriteLockedByCurrentThread() ? writeHolds_ : 0;
        }
