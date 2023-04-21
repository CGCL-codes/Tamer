    public RemoteFileState storeSuperceded(RemoteFileState in) throws Exception {
        if (trialRun) return in;
        if (in.remote_file_state_id == 0) throw new IllegalArgumentException("Should have database id for " + in.url);
        switch(in.state) {
            case SUCCESS_ARCHIVED:
            case IGNORED_ARCHIVED:
            case SUPERCEDED_ARCHIVED:
            case DUPLICATE_ARCHIVED:
            case FAILURE_ARCHIVED:
                throw new IllegalStateException("Cannot mark this file as superceded (" + in.state.toString() + ").");
            default:
                in.state = FileStates.SUPERCEDED;
                break;
        }
        Db db = getDb();
        try {
            db.begin();
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
