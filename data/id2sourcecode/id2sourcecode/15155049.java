    public LocalFileState storeArchived(LocalFileState in) throws Exception {
        if (trialRun) return in;
        if (in == null) return null;
        synchronized (lock) {
            switch(in.state) {
                case RUNNING:
                case READY:
                case SUCCESS:
                    in.state = FileStates.SUCCESS_ARCHIVED;
                    break;
                case IGNORED:
                    in.state = FileStates.IGNORED_ARCHIVED;
                    break;
                case SUPERCEDED:
                    in.state = FileStates.SUPERCEDED_ARCHIVED;
                    break;
                case DUPLICATE:
                    in.state = FileStates.DUPLICATE_ARCHIVED;
                    break;
                case FAILURE:
                    in.state = FileStates.FAILURE_ARCHIVED;
                    break;
                default:
                    return in;
            }
            in.to_archive = false;
            in.to_errors_archive = false;
            Db db = getDb();
            try {
                db.begin();
                if (in.local_file_state_id == 0) throw new IllegalArgumentException("Should have database id for " + in.filename);
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
