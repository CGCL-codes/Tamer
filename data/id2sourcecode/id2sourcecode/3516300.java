    protected void _downloadFile(InputStream reader, byte first_char) throws IOException, java.io.FileNotFoundException {
        TransferStatus ts = null;
        long downloaded_so_far = 0;
        synchronized (_download_lock) {
            try {
                _setUserConnectionState(UserConnectionState.DL_DOWNLOADING);
                downloaded_so_far = 0;
                long file_size = _downloadFileSize();
                ts = new TransferStatus(_dr.getOwnerNick(), _dr.getDestinationPath(), _dr.getDRUCMapId(), TransferStatus._WAITING, TransferStatus._DOWNLOAD, file_size, 0);
                _dr.setStatus(DownloadRequest._RUNNING);
                _user_cb_handler.execute(UserConnectionHandler.TRANSFER_STATUS_CREATED_CB, ts);
                OutputStream os = null;
                File save_file = null;
                if (_dr.getFileType() == DownloadRequest.FILE_TYPE_NORMAL) {
                    save_file = new File(_dr.getDestinationPath());
                    save_file.createNewFile();
                    os = new FileOutputStream(save_file);
                } else {
                    os = new ByteArrayOutputStream((int) file_size);
                }
                int receiverBufferSize = (int) Configuration.instance().getReceiveBufferSize();
                byte[] read_arr = new byte[receiverBufferSize];
                read_arr[0] = first_char;
                os.write(read_arr, 0, 1);
                downloaded_so_far = 1;
                ts.setStatus_Progress(TransferStatus._RUNNING, downloaded_so_far);
                _debug("downloaded_so_far: " + downloaded_so_far);
                _debug("file_size: " + file_size);
                while (downloaded_so_far < file_size && _dr.getActive()) {
                    int nr_of_chars_read = reader.read(read_arr, 0, read_arr.length);
                    if (nr_of_chars_read == -1) break;
                    os.write(read_arr, 0, nr_of_chars_read);
                    downloaded_so_far += nr_of_chars_read;
                    ts.setProgress(downloaded_so_far);
                    _user_cb_handler.execute(FILE_DOWNLOAD_PROGRESS_CB, ts);
                }
                os.flush();
                os.close();
                _fileDownloadMode(false);
                _downloadFileSize(0);
                if (_dr.getActive()) {
                    _debug("File download complete...");
                    ts.setStatus_Progress(TransferStatus._FINISHED, downloaded_so_far);
                    Object data = save_file;
                    if (_dr.getFileType() == DownloadRequest.FILE_TYPE_FILE_LIST) {
                        data = _createTree((ByteArrayOutputStream) os);
                    } else {
                    }
                    _cb_handler.execute(FILE_DOWNLOAD_DONE_CB, new DownloadedFile(_dr, data));
                    EventDispatcher.instance().dispatchEvent(new DownloadEvent(_dr.getIdStr(), _dr, DownloadEvent.DONE));
                } else {
                    _debug("File download aborted...");
                    ts.setStatus_Progress(TransferStatus._ABORTED, downloaded_so_far);
                    _cb_handler.execute(FILE_DOWNLOAD_ABORTED_CB, _dr);
                    EventDispatcher.instance().dispatchEvent(new DownloadEvent(_dr.getIdStr(), _dr, DownloadEvent.ABORTED));
                    disconnectNoThread();
                    _dispatchRelocateDLQueueEvent();
                }
            } catch (IOException ioEx) {
                ts.setStatus_Progress(TransferStatus._FAILURE, downloaded_so_far);
                EventDispatcher.instance().dispatchEvent(new DownloadEvent(_dr.getIdStr(), _dr, DownloadEvent.FAILED, DownloadEvent.REASON_CONNECTION_CLOSED));
            } catch (IllegalStateTransition ex) {
                Configuration.instance().executeExceptionCallback(ex);
                ts.setStatus_Progress(TransferStatus._FAILURE, downloaded_so_far);
                EventDispatcher.instance().dispatchEvent(new DownloadEvent(_dr.getIdStr(), _dr, DownloadEvent.FAILED));
                libLogger.debug("Exception _downloadFile", ex);
            } finally {
                checkTime cleaner = new checkTime(10, ts, "ts cleaner");
                try {
                    _setUserConnectionState(UserConnectionState.NONE);
                } catch (IllegalStateTransition ex) {
                    Configuration.instance().executeExceptionCallback(ex);
                    libLogger.debug("Exception _downloadFile", ex);
                }
                _download_lock.notifyAll();
            }
        }
    }
