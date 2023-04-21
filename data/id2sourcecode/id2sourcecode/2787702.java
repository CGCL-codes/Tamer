    private void spinWait(boolean fromTail) {
        long startNano = 0;
        long lastElapsedNano = 0;
        while (true) {
            long startCycles = VM.statistics.cycles();
            long endCycles = startCycles + ((long) 1e9);
            long nowCycles;
            do {
                VM.memory.isync();
                Address rtn = ((fromTail) ? tail : head);
                if (!rtn.isZero() || complete()) return;
                nowCycles = VM.statistics.cycles();
            } while (startCycles < nowCycles && nowCycles < endCycles);
            lock();
            if (startNano == 0) {
                startNano = VM.statistics.nanoTime();
            } else {
                long nowNano = VM.statistics.nanoTime();
                long elapsedNano = nowNano - startNano;
                if (elapsedNano - lastElapsedNano > WARN_PERIOD) {
                    Log.write("GC Warning: SharedDeque(");
                    Log.write(name);
                    Log.write(") wait has reached ");
                    Log.write(VM.statistics.nanosToSecs(elapsedNano));
                    Log.write(", ");
                    Log.write(numConsumersWaiting);
                    Log.write("/");
                    Log.write(numConsumers);
                    Log.writeln(" threads waiting");
                    lastElapsedNano = elapsedNano;
                }
                if (elapsedNano > TIMEOUT_PERIOD) {
                    unlock();
                    VM.assertions.fail("GC Error: SharedDeque Timeout");
                }
            }
            unlock();
        }
    }
