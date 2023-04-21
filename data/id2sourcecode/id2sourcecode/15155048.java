    public LocalFileState storeOutcome(LocalFileState in, boolean skipped, boolean ran, boolean successOrFailure, boolean error, Exception ex, String _message) throws Exception {
        if (trialRun) return in;
        String message = _message;
        synchronized (lock) {
            Db db = getDb();
            try {
                db.begin();
                if (in == null) return null;
                if (in.local_file_state_id == 0) throw new IllegalArgumentException("Should have database id for " + in.filename);
                if (skipped) {
                    if (!ran) {
                        in.state = FileStates.IGNORED;
                        in.retry_count = 0;
                    }
                }
                if (ran) {
                    if (successOrFailure) {
                        in.state = FileStates.SUCCESS;
                    } else {
                        if (error) {
                            in.state = FileStates.ERROR_RETRY;
                            in.retry_count++;
                        } else {
                            in.state = FileStates.FAILURE;
                        }
                    }
                }
                if (message == null && ex != null) message = ex.getMessage();
                int n = 1;
                ps_update.setInt(n++, in.state.getId());
                ps_update.setInt(n++, in.retry_count);
                ps_update.setString(n++, message);
                ps_update.setObject(n, null);
                if (ran && in.pf != null) {
                    ps_update.setString(n, in.pf.getClassName());
                }
                ++n;
                ps_update.setInt(n++, in.local_file_state_id);
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
    }
