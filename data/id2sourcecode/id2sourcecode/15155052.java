    public LocalFileState storeIgnored(LocalFileState in) throws Exception {
        if (trialRun) return in;
        if (in.local_file_state_id == 0) throw new IllegalArgumentException("Should have database id for " + in.filename);
        if (in.state.getId() > 0 && in.state != FileStates.IGNORED) {
            throw new IllegalStateException("Cannot mark this file as ignored " + in.state);
        }
        in.state = FileStates.IGNORED;
        synchronized (lock) {
            Db db = getDb();
            try {
                db.begin();
                ps_update_state.setInt(1, in.state.getId());
                ps_update_state.setInt(2, in.local_file_state_id);
                int upd = db.executeUpdate(ps_update_state);
                if (upd != 1) {
                    _logger.warn("Error updating file state for " + in.filename);
                }
                updateMetadataInTx(in);
                return in;
            } catch (Exception e) {
                db.rollback();
                throw e;
            } finally {
                db.commitUnless();
            }
        }
    }
