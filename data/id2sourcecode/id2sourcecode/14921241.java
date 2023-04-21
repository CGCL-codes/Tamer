    public static void markProbeFinished(Db statusDb, ProbeBase probeBase, boolean success, boolean error, Exception ex, String message, boolean trialRun) throws Exception {
        _logger.debug("Finalizing probe (" + probeBase.getClass().getName() + ") with (success: " + success + ", error: " + error + ", exception: " + (ex == null ? "null" : ex.getMessage()) + ", msg :" + message + ", trial: " + trialRun + ")");
        String msg = (ex != null && message == null ? StringHelper.getMessage(ex) : message);
        String stackTrace = (ex == null ? "" : StringHelper.getStackTraceAsString(ex));
        try {
            statusDb.begin();
            Integer state = DbHelper.getKey(statusDb.prepareStatement("SELECT e_probe_run_status_id FROM e_probe_history WHERE e_probe_history_id=" + probeBase.getProbeHistoryId()));
            if (state == null) throw new IllegalStateException("Trying to mark as finished an invalid probe history record (" + probeBase.getProbeHistoryId() + ")");
            if (state != ProbeStatus.STILL_RUNNING && state != ProbeStatus.NO_DATA) throw new IllegalStateException("Trying to mark as finished an already closed (" + state + ") probe history record (" + probeBase.getProbeHistoryId() + ")");
            Integer thread = DbHelper.getKey(statusDb.prepareStatement("SELECT thread FROM e_probe_pid WHERE lower(probe_name) = lower('" + probeBase.getClass().getName() + "')"));
            if (thread == null) throw new IllegalStateException("Finalizing a probe (" + probeBase.getClass().getName() + ") which is not running");
            if (thread.intValue() != 1) throw new IllegalStateException("Finalizing a probe (" + probeBase.getClass().getName() + ") with abnormal thread count (" + thread + ")");
            synchronized (lock) {
                removeLock(statusDb, probeBase);
            }
            PreparedStatement ps_finish_elt = statusDb.prepareStatement("UPDATE e_probe_history SET end_date=current_timestamp, success=?, error=?, error_message=?, exception_stacktrace=?, e_probe_run_status_id = ? WHERE e_probe_history_id=?");
            ps_finish_elt.setBoolean(1, success);
            ps_finish_elt.setBoolean(2, error);
            DbHelper.setSafeString(ps_finish_elt, 3, msg);
            DbHelper.setSafeString(ps_finish_elt, 4, stackTrace);
            int status = ProbeStatus.FAILURE;
            if (success) status = ProbeStatus.SUCCESS;
            if (error) status = ProbeStatus.ERROR;
            ps_finish_elt.setInt(5, status);
            ps_finish_elt.setInt(6, probeBase.getProbeHistoryId());
            statusDb.executeUpdate(ps_finish_elt);
            saveProbeParameters(statusDb, probeBase);
            if (probeBase instanceof FileProbe) {
                FileProbe fp = (FileProbe) probeBase;
                updateFileProbeHistory(statusDb, fp);
                if (fp.isAssetProbe()) {
                    fillAssetProbeHistory(statusDb, fp, fp.getProbeHistoryId());
                }
            }
            Incident i = new Incident();
            i.setProbeName(probeBase.getClass().getName());
            i.setEsisVersion(Version.getVersionString());
            i.setLastException(stackTrace);
            List<Exception> exs = new ArrayList<Exception>();
            if (ex != null) exs.add(ex);
            if (status == ProbeStatus.FAILURE && probeBase.isSendIncidentWhenFailure()) {
                _logger.warn("Probe " + probeBase.getClass().getName() + " ended with failure : generating incident due to probe configuration");
                i.setSummary("Probe " + probeBase.getClass().getName() + " failed to succeed");
                i.setProbeMessage("Probe " + probeBase.getClass().getName() + " failed to succeed");
                DbIncident.saveIncident(statusDb, i, null);
            } else if (status == ProbeStatus.ERROR && probeBase.isSendIncidentWhenError()) {
                _logger.warn("Probe " + probeBase.getClass().getName() + " ended with error : generating incident due to probe configuration");
                i.setSummary("Probe " + probeBase.getClass().getName() + " error : " + msg);
                i.setProbeMessage("Probe " + probeBase.getClass().getName() + " error : " + msg);
                DbIncident.saveIncident(statusDb, i, null);
            }
        } catch (Exception e) {
            _logger.error("Exception while setting probe to finished with message [" + msg + "] " + probeBase.getClass().getName(), e);
            statusDb.rollback();
            throw e;
        } finally {
            statusDb.commitUnless();
        }
    }
