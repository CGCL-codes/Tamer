    protected synchronized boolean threadRequestsSleeping(AWorkerThread exe_, double time_) {
        if (resetting) return true;
        nthreadsWaiting++;
        wakeupThread.Q.enqueue(time_, exe_.sleepOn);
        if (logenabled) {
            if (tf == null) _openlog();
            try {
                tf.write(("+ " + time_ + "\t\t" + wakeupThread.Q.getLength() + "\n").toCharArray());
                tf.flush();
            } catch (Exception e_) {
            }
        }
        if (maxlength < wakeupThread.Q.getLength()) maxlength = wakeupThread.Q.getLength();
        if (debug && isDebugEnabledAt(Debug_Q)) println(Debug_Q, exe_, "Enqueue thread(threadRequestsSleeping()), " + "sleepOn:" + exe_.sleepOn);
        if (nthreadsWaiting == vWorking.size()) {
            if (qReady.isEmpty() && wakeupThread.Q.firstElement() == exe_.sleepOn) {
                systemBecomesIdle(exe_);
                if (time_ - _getTime() <= timeScaleReciprocal) {
                    if (debug && isDebugEnabledAt(Debug_Q)) println(Debug_Q, null, "DONT_SLEEP (remove from Q):" + exe_.sleepOn);
                    wakeupThread.Q.dequeue();
                    if (logenabled) {
                        if (tf == null) _openlog();
                        try {
                            tf.write(("-\t\t\t" + wakeupThread.Q.getLength() + "\n").toCharArray());
                            tf.flush();
                        } catch (Exception e_) {
                        }
                    }
                    nthreadsWaiting--;
                    return false;
                }
            }
        }
        returnWorkforce();
        _startAll(null);
        if (timeScaleReciprocal > 0.0 && wakeupThread.Q.firstElement() == exe_.sleepOn) triggerWakeupThread();
        return true;
    }
