    public synchronized RemoteFileState storeFetched(RemoteFileState in, MirrorReturn fetched, Exception exOrNull) throws Exception {
        _logger.trace("Storing as fetched (" + in.filename + ")");
        if (trialRun) return in;
        Db db = getDb();
        try {
            db.begin();
            boolean success = (fetched != null);
            Integer localFileStateId = null;
            if (fetched != null) {
                localFileStateId = fetched.local.local_file_state_id;
            }
            if (success) {
                in.state = FileStates.SUCCESS;
            } else {
                if (exOrNull == null) {
                    in.state = FileStates.FAILURE;
                } else {
                    in.state = FileStates.ERROR_RETRY;
                    in.retry_count++;
                }
            }
            ps_update.setInt(1, in.state.getId());
            ps_update.setInt(2, in.retry_count);
            ps_update.setLong(3, in.last_modified);
            String m = null;
            if (exOrNull != null) m = exOrNull.getMessage();
            ps_update.setString(4, m);
            ps_update.setObject(5, localFileStateId);
            ps_update.setInt(6, in.remote_file_state_id);
            db.executeUpdate(ps_update);
            updateMetadataInTx(in);
            return in;
        } catch (Exception e) {
            db.rollback();
            throw e;
        } finally {
            db.commitUnless();
        }
    }
