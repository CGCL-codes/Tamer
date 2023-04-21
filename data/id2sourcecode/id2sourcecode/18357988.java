        protected void doRequest(final DiskAccessRequestListener l) throws CacheFileManagerException {
            Object[] stuff = (Object[]) chunks.get(chunk_index++);
            final DiskManagerFileInfoImpl file = (DiskManagerFileInfoImpl) stuff[0];
            buffer.limit(DirectByteBuffer.SS_DR, ((Integer) stuff[2]).intValue());
            if (file.getAccessMode() == DiskManagerFileInfo.READ) {
                if (Logger.isEnabled()) Logger.log(new LogEvent(disk_manager, LOGID, "Changing " + file.getFile(true).getName() + " to read/write"));
                file.setAccessMode(DiskManagerFileInfo.WRITE);
            }
            boolean handover_buffer = chunk_index == chunks.size();
            DiskAccessRequestListener delegate_listener = new DiskAccessRequestListener() {

                public void requestComplete(DiskAccessRequest request) {
                    l.requestComplete(request);
                    file.dataWritten(request.getOffset(), request.getSize());
                }

                public void requestCancelled(DiskAccessRequest request) {
                    l.requestCancelled(request);
                }

                public void requestFailed(DiskAccessRequest request, Throwable cause) {
                    l.requestFailed(request, cause);
                }

                public int getPriority() {
                    return (-1);
                }

                public void requestExecuted(long bytes) {
                }
            };
            disk_access.queueWriteRequest(file.getCacheFile(), ((Long) stuff[1]).longValue(), buffer, handover_buffer, delegate_listener);
        }
