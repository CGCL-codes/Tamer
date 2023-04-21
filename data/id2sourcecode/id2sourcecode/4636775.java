    public int executeUpdate() throws SQLException {
        long start = startTime = System.currentTimeMillis();
        Database database = session.getDatabase();
        MemoryUtils.allocateReserveMemory();
        Object sync = database.isMultiThreaded() ? (Object) session : (Object) database;
        session.waitIfExclusiveModeEnabled();
        synchronized (sync) {
            int rollback = session.getLogId();
            session.setCurrentCommand(this, startTime);
            try {
                while (true) {
                    database.checkPowerOff();
                    try {
                        return update();
                    } catch (SQLException e) {
                        if (e.getErrorCode() == ErrorCode.CONCURRENT_UPDATE_1) {
                            long now = System.currentTimeMillis();
                            if (now - start > session.getLockTimeout()) {
                                throw Message.getSQLException(ErrorCode.LOCK_TIMEOUT_1, e, "");
                            }
                            try {
                                if (sync == database) {
                                    database.wait(100);
                                } else {
                                    Thread.sleep(100);
                                }
                            } catch (InterruptedException e1) {
                            }
                            continue;
                        }
                        throw e;
                    } catch (Exception e) {
                        throw Message.convert(e);
                    } catch (Throwable e) {
                        throw Message.convertThrowable(e);
                    }
                }
            } catch (SQLException e) {
                Message.addSQL(e, sql);
                database.exceptionThrown(e, sql);
                database.checkPowerOff();
                if (e.getErrorCode() == ErrorCode.DEADLOCK_1) {
                    session.rollback();
                } else if (e.getErrorCode() == ErrorCode.OUT_OF_MEMORY) {
                    try {
                        session.rollbackTo(rollback, true);
                    } catch (SQLException e2) {
                        if (e2.getErrorCode() == ErrorCode.OUT_OF_MEMORY) {
                            session.getDatabase().shutdownImmediately();
                        }
                        throw e2;
                    }
                } else {
                    session.rollbackTo(rollback, false);
                }
                throw e;
            } finally {
                stop();
            }
        }
    }
