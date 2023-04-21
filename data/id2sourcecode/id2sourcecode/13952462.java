    boolean remove(Process process) {
        readyList_.removeElement(process);
        boolean done = false;
        int i = 0;
        while (!done) if (process_[i].getProcessId() == process.getProcessId()) done = true; else i++;
        for (; i < process_.length - 1; i++) process_[i] = process_[i + 1];
        process_[i] = process;
        return readyList_.isEmpty();
    }
