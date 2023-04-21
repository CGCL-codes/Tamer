    public LocalFileState storeSuperceded(LocalFileState in) throws Exception {
        if (trialRun) return in;
        if (in.local_file_state_id == 0) throw new IllegalArgumentException("Should have database id for " + in.filename);
        synchronized (lock) {
            switch(in.state) {
                case SUCCESS_ARCHIVED:
                case IGNORED_ARCHIVED:
                case SUPERCEDED_ARCHIVED:
                case DUPLICATE_ARCHIVED:
                case FAILURE_ARCHIVED:
                    throw new IllegalStateException("Cannot mark this file as superceded (" + in.state.toString() + ")");
                default:
                    in.state = FileStates.SUPERCEDED;
                    break;
            }
            Db db = getDb();
            try {
                db.begin();
                ps_update_state.setInt(1, in.state.getId());
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
