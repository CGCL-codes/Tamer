    protected synchronized void newTask(Task task_, WorkerThread currentThread_) {
        if (resetting || task_ == null) return;
        AWorkerThread current_ = (AWorkerThread) currentThread_;
        if (debug && isDebugEnabledAt(Debug_Q) && current_ != null) System.out.println(_getTime() + ": qReady.length=" + qReady.getLength() + ", current=" + current_.state);
        if (current_ != null && !current_.isReadyForNextTask()) current_ = null;
        if (task_.time > _getTime()) {
            if (debug && isDebugEnabledAt(Debug_THREAD)) println(Debug_THREAD, current_, "to waiting-queue:" + task_);
            wakeupThread.Q.enqueue(task_.time, task_);
            if (logenabled) {
                if (tf == null) _openlog();
                try {
                    tf.write(("+ " + task_.time + "\t\t" + wakeupThread.Q.getLength() + "\n").toCharArray());
                    tf.flush();
                } catch (Exception e_) {
                }
            }
            if (maxlength < wakeupThread.Q.getLength()) maxlength = wakeupThread.Q.getLength();
            if (timeScaleReciprocal > 0.0 && wakeupThread.Q.firstElement() == task_) triggerWakeupThread();
            if (state == State_INACTIVE) _startAll(null);
            return;
        }
        if (current_ != null && qReady.isEmpty()) {
            if (debug && isDebugEnabledAt(Debug_THREAD)) println(Debug_THREAD, current_, "Assign task:" + task_ + " to current thread");
            current_.nextTask = task_;
            return;
        }
        synchronized (this) {
            if (!qReady.isEmpty() || (current_ == null && !getWorkforce())) {
                if (debug && isDebugEnabledAt(Debug_THREAD)) println(Debug_THREAD, current_, "Enqueue task:" + task_);
                qReady.enqueue(task_);
                return;
            }
            if (current_ != null) {
                if (debug && isDebugEnabledAt(Debug_THREAD)) println(Debug_THREAD, current_, "Assign task:" + task_ + " to current thread");
                current_.nextTask = task_;
            } else {
                current_ = grabOne();
                if (debug && isDebugEnabledAt(Debug_THREAD)) println(Debug_THREAD, null, "Assign task:" + task_ + " to inactive thread:" + current_._getName());
                synchronized (current_) {
                    current_.mainContext = task_;
                    current_.start();
                }
            }
        }
    }
