    public static void markFinished(Db statusDb, ProbeBase probeBase, boolean success, boolean error, boolean trialRun) throws Exception {
        _logger.debug("Marking probe (" + probeBase.getClass().getName() + ") has finished with status (success: " + success + ", error: " + error + ") and trial (" + trialRun + ")");
        if (trialRun) return;
        if (success && error) throw new IllegalArgumentException("Cannot have both success and error set!");
        _logger.debug("Entering in synchronized section");
        synchronized (lock) {
            try {
                statusDb.begin();
                Integer thread = DbHelper.getKey(statusDb.prepareStatement("SELECT thread FROM e_probe_pid WHERE lower(probe_name) = lower('" + probeBase.getClass().getName() + "')"));
                if (thread == null) throw new IllegalStateException("Trying to mark as finished a non running probe (" + probeBase.getClass().getName() + ")");
                if (thread.intValue() > 1) {
                    _logger.trace("Decreasing thread count (" + (thread.intValue() + 1) + ") by one for probe (" + probeBase.getClass().getName() + ")");
                    PreparedStatement remove_thread = statusDb.prepareStatement("UPDATE e_probe_pid SET thread -= 1 WHERE lower(probe_name) = lower(?)");
                    remove_thread.setString(1, probeBase.getClass().getName());
                    statusDb.executeUpdate(remove_thread);
                }
            } catch (Exception e) {
                _logger.error("Exception while removing probe PID or decreasing thread count " + probeBase.getClass().getName(), e);
                statusDb.rollback();
                throw e;
            } finally {
                _logger.debug("Leaving synchronized section");
                statusDb.commitUnless();
            }
        }
    }
