    public void writeBlock(final DiskManagerWriteRequest request, final DiskManagerWriteRequestListener _listener) {
        request.requestStarts();
        final DiskManagerWriteRequestListener listener = new DiskManagerWriteRequestListener() {

            public void writeCompleted(DiskManagerWriteRequest request) {
                request.requestEnds(true);
                _listener.writeCompleted(request);
            }

            public void writeFailed(DiskManagerWriteRequest request, Throwable cause) {
                request.requestEnds(false);
                _listener.writeFailed(request, cause);
            }
        };
        try {
            int pieceNumber = request.getPieceNumber();
            DirectByteBuffer buffer = request.getBuffer();
            int offset = request.getOffset();
            final DiskManagerPiece dmPiece = disk_manager.getPieces()[pieceNumber];
            if (dmPiece.isDone()) {
                buffer.returnToPool();
                listener.writeCompleted(request);
            } else {
                int buffer_position = buffer.position(DirectByteBuffer.SS_DW);
                int buffer_limit = buffer.limit(DirectByteBuffer.SS_DW);
                int previousFilesLength = 0;
                int currentFile = 0;
                DMPieceList pieceList = disk_manager.getPieceList(pieceNumber);
                DMPieceMapEntry current_piece = pieceList.get(currentFile);
                long fileOffset = current_piece.getOffset();
                while ((previousFilesLength + current_piece.getLength()) < offset) {
                    previousFilesLength += current_piece.getLength();
                    currentFile++;
                    fileOffset = 0;
                    current_piece = pieceList.get(currentFile);
                }
                List chunks = new ArrayList();
                while (buffer_position < buffer_limit) {
                    current_piece = pieceList.get(currentFile);
                    long file_limit = buffer_position + ((current_piece.getFile().getLength() - current_piece.getOffset()) - (offset - previousFilesLength));
                    if (file_limit > buffer_limit) {
                        file_limit = buffer_limit;
                    }
                    if (file_limit > buffer_position) {
                        long file_pos = fileOffset + (offset - previousFilesLength);
                        chunks.add(new Object[] { current_piece.getFile(), new Long(file_pos), new Integer((int) file_limit) });
                        buffer_position = (int) file_limit;
                    }
                    currentFile++;
                    fileOffset = 0;
                    previousFilesLength = offset;
                }
                DiskManagerWriteRequestListener l = new DiskManagerWriteRequestListener() {

                    public void writeCompleted(DiskManagerWriteRequest request) {
                        complete();
                        listener.writeCompleted(request);
                    }

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

                    protected void complete() {
                        try {
                            this_mon.enter();
                            async_writes--;
                            if (!write_requests.remove(request)) {
                                Debug.out("request not found");
                            }
                            if (stopped) {
                                async_write_sem.release();
                            }
                        } finally {
                            this_mon.exit();
                        }
                    }
                };
                try {
                    this_mon.enter();
                    if (stopped) {
                        buffer.returnToPool();
                        listener.writeFailed(request, new Exception("Disk writer has been stopped"));
                        return;
                    } else {
                        async_writes++;
                        write_requests.add(request);
                    }
                } finally {
                    this_mon.exit();
                }
                new requestDispatcher(request, l, buffer, chunks);
            }
        } catch (Throwable e) {
            request.getBuffer().returnToPool();
            disk_manager.setFailed("Disk write error - " + Debug.getNestedExceptionMessage(e));
            Debug.printStackTrace(e);
            listener.writeFailed(request, e);
        }
    }
