    public synchronized RemoteFileState storeIgnored(RemoteFileState in) throws Exception {
        _logger.trace("Storing as ignored (" + in.filename + ")");
        if (trialRun) return in;
        Db db = getDb();
        try {
            db.begin();
            in.state = FileStates.IGNORED;
            ps_update_state.setInt(1, in.state.getId());
            ps_update_state.setInt(2, in.remote_file_state_id);
            db.executeUpdate(ps_update_state);
            updateMetadataInTx(in);
            return in;
        } catch (Exception e) {
            db.rollback();
            throw e;
        } finally {
            db.commitUnless();
        }
    }
