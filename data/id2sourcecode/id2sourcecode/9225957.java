        public void run() {
            if (Thread.currentThread() != this) return;
            try {
                synchronized (runtime) {
                    for (; ; ) {
                        while (Q.isEmpty() || isSuspend()) {
                            wakeupThreadState = isSuspend() ? "suspended" : "wait";
                            runtime.wait();
                            if (debug && isDebugEnabledAt(Debug_Q)) println(Debug_Q, null, "wakeupthread awaked from indefinite sleep");
                        }
                        next = null;
                        double time_ = Q.firstKey();
                        double now_ = runtime._getTime();
                        if (time_ - now_ <= timeScaleReciprocal) {
                            next = Q.dequeue();
                            if (logenabled) {
                                if (tf == null) _openlog();
                                try {
                                    tf.write(("-\t\t\t" + wakeupThread.Q.getLength() + "\n").toCharArray());
                                    tf.flush();
                                } catch (Exception e_) {
                                }
                            }
                            if (debug && isDebugEnabledAt(Debug_Q)) println(Debug_Q, null, "Dequeue sleepOn:" + next);
                        }
                        if (next != null) {
                            if (next instanceof Task) {
                                ((Task) next).time = 0.0;
                                newTask((Task) next, null);
                            } else newTask(Task.createNotify(next), null);
                        } else if (!isSuspend()) {
                            realTime = timeScale * (time_ - now_);
                            if (realTime > MAX_SLEEP_TIME_THAT_MAKES_SENSE) {
                                if (debug && isDebugEnabledAt(Debug_Q)) wakeupThreadState = "now=" + now_ + ", sleep_until:" + time_ + " (walltime=" + MAX_SLEEP_TIME_THAT_MAKES_SENSE + ")"; else wakeupThreadState = "sleep_until";
                                runtime.wait((long) MAX_SLEEP_TIME_THAT_MAKES_SENSE);
                            } else {
                                if (debug && isDebugEnabledAt(Debug_Q)) wakeupThreadState = "now=" + now_ + ", sleep_until:" + time_ + " (walltime=" + realTime + ")"; else wakeupThreadState = "sleep_until";
                                runtime.wait((long) realTime);
                            }
                            if (debug && isDebugEnabledAt(Debug_Q)) println(Debug_Q, null, "wakeupthread awaked");
                        }
                    }
                }
            } catch (Throwable e_) {
                e_.printStackTrace();
                drcl.Debug.systemFatalError("ARuntime.wakeupThread is corrupted, " + "forcing JavaSim to exit!");
            }
        }
