    public static int markStarted(Db statusDb, FileProbe fileProbe, LocalFileState lfs, boolean trialRun) throws ControlProblem, Exception {
        if (lfs == null) throw new IllegalArgumentException("The LocalFileState cannot be null");
        _logger.debug("Marking FileProbe (" + fileProbe.getClass().getName() + ") has running with " + lfs + " in trial (" + trialRun + ")");
        if (trialRun) return -1;
        _logger.debug("Entering in synchronized section");
        synchronized (lock) {
            try {
                statusDb.begin();
                if (getProbePID(statusDb, fileProbe.getClass().getName()) == null) {
                    createLock(statusDb, fileProbe);
                    setProbeRunning(statusDb, fileProbe);
                    unstartedProbes.remove(fileProbe);
                } else {
                    PreparedStatement add_thread = statusDb.prepareStatement("UPDATE e_probe_pid SET thread=1 WHERE lower(probe_name) = lower(?)");
                    add_thread.setString(1, fileProbe.getClass().getName());
                    statusDb.executeUpdate(add_thread);
                }
                Integer phId = DbHelper.getKey(statusDb.prepareStatement("SELECT probe_history_id FROM e_local_file_state WHERE local_file_state_id=" + lfs.local_file_state_id));
                if (phId != null && phId.intValue() != fileProbe.getProbeHistoryId()) _logger.warn("The local file state id (" + lfs.local_file_state_id + ") is already linked to probe history id (" + fileProbe.getProbeHistoryId() + ")");
                PreparedStatement ps_start_file1 = statusDb.prepareStatement("UPDATE e_local_file_state SET probe_history_id=? WHERE local_file_state_id=?");
                ps_start_file1.setInt(1, fileProbe.getProbeHistoryId());
                ps_start_file1.setInt(2, lfs.local_file_state_id);
                int res = statusDb.executeUpdate(ps_start_file1);
                if (res != 1) throw new IllegalStateException("Unable to update the local file state record (" + lfs.local_file_state_id + ") for probe history record (" + fileProbe.getProbeHistoryId() + ")");
                PreparedStatement ps_start_file2 = statusDb.prepareStatement("INSERT INTO e_probe_file_history (e_probe_history_id, file_name, file_size, e_local_file_state_id) VALUES (?, ?, ?, ?)");
                ps_start_file2.setInt(1, fileProbe.getProbeHistoryId());
                ps_start_file2.setString(2, lfs.file.getAbsolutePath());
                ps_start_file2.setLong(3, lfs.file.length());
                ps_start_file2.setLong(4, lfs.local_file_state_id);
                res = statusDb.executeUpdate(ps_start_file2);
                if (res != 1) throw new IllegalStateException("Unable to insert probe file history record for file (" + lfs.file.getAbsolutePath() + ") and probe history record (" + fileProbe.getProbeHistoryId() + ")");
                if (fileProbe.isAssetProbe()) {
                    fillAssetProbeDetails(statusDb, fileProbe);
                }
                return fileProbe.getProbeHistoryId();
            } catch (Exception e) {
                statusDb.rollback();
                throw e;
            } finally {
                statusDb.commitUnless();
                _logger.debug("Leaving synchronized section");
            }
        }
    }
