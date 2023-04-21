    public int countOfStepThreadsAlive(ETLStep writer) {
        int cnt = 0;
        for (Object o : this.threads) {
            WorkerThread wrk = (WorkerThread) o;
            if (wrk.thread.isAlive() && wrk.step.mstrName.endsWith(writer.getName())) cnt++;
        }
        return cnt;
    }
