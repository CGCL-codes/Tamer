    private void _startAll(AWorkerThread current_) {
        if (qReady.isEmpty()) {
            systemBecomesIdle(current_);
            if (!wakeupThread.Q.isEmpty()) {
                boolean changed_ = false;
                while (wakeupThread.Q.firstKey() - _getTime() <= timeScaleReciprocal) {
                    Object o_ = wakeupThread.Q.dequeue();
                    if (logenabled) {
                        if (tf == null) _openlog();
                        try {
                            tf.write(("-\t\t\t" + wakeupThread.Q.getLength() + "\n").toCharArray());
                            tf.flush();
                        } catch (Exception e_) {
                        }
                    }
                    changed_ = true;
                    if (o_ instanceof Task) {
                        newTask((Task) o_, current_);
                    } else newTask(Task.createNotify(o_), current_);
                    if (current_ != null && current_.nextTask != null) {
                        if (current_.mainContext == null) {
                            if (!getWorkforce()) drcl.Debug.systemFatalError("No workforce for even one thread!?");
                        }
                        current_.mainContext = current_.nextTask;
                        current_.nextTask = null;
                        current_ = null;
                    }
                }
                if (timeScaleReciprocal > 0.0 && changed_ && !wakeupThread.Q.isEmpty()) triggerWakeupThread();
            }
        }
        Task task_ = null;
        if (current_ != null) {
            task_ = getTask(current_.mainContext == null);
            if (task_ != null) current_.mainContext = task_;
        }
        while ((task_ = getTask(true)) != null) {
            AWorkerThread t_ = grabOne();
            if (debug && isDebugEnabledAt(Debug_THREAD)) println(Debug_THREAD, null, "Assign to inactive thread, task:" + task_ + ", thread:" + t_);
            synchronized (t_) {
                t_.mainContext = task_;
                t_.start();
            }
        }
        if (state == State_INACTIVE) {
            setState(State_RUNNING);
            adjustTime();
        }
    }
