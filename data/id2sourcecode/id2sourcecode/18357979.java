                    public void writeFailed(DiskManagerWriteRequest request, Throwable cause) {
                        complete();
                        if (dmPiece.isDone()) {
                            if (Logger.isEnabled()) {
                                Logger.log(new LogEvent(disk_manager, LOGID, "Piece " + dmPiece.getPieceNumber() + " write failed but already marked as done"));
                            }
                            listener.writeCompleted(request);
                        } else {
                            disk_manager.setFailed("Disk write error - " + Debug.getNestedExceptionMessage(cause));
                            Debug.printStackTrace(cause);
                            listener.writeFailed(request, cause);
                        }
                    }
