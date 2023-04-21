    protected synchronized void _uploadFile(String file_path) {
        File upload_file = null;
        TransferStatus ts = null;
        if (getUploadMode().equals(UploadMode.FILE_LIST_UL)) {
            _debug("Uploading my file list...");
            if (file_path.equals("MyList.bz2")) {
                upload_file = Configuration.instance().getUser().getShareStorage().getMyBZipCompressedList();
            } else {
                upload_file = Configuration.instance().getUser().getShareStorage().getMyCompressedList();
            }
        } else {
            _debug("Uploading " + file_path + "...");
            upload_file = new File(file_path);
        }
        if (!upload_file.canRead() || !upload_file.exists() || !upload_file.isFile() || upload_file.length() == 0L) {
            libLogger.error("upload_file.canRead(): " + upload_file.canRead());
            libLogger.error("upload_file.exists(): " + upload_file.exists());
            libLogger.error("upload_file.isFile(): " + upload_file.isFile());
            libLogger.error("upload_file.length(): " + upload_file.length());
            libLogger.error("This file is rotten...");
            disconnect();
            return;
        }
        int sendBufferSize = (int) Configuration.instance().getSendBufferSize();
        synchronized (_socket) {
            byte byte_arr[] = new byte[sendBufferSize];
            long uploaded_so_far = 0;
            long file_size = upload_file.length();
            int nr_of_chars_read = 0;
            UploadListEntry ull_entry = new UploadListEntry(_connected_to, upload_file.getName());
            UploadList.instance().put(ull_entry);
            ts = new TransferStatus(_connected_to, upload_file.getName(), ull_entry.id(), TransferStatus._WAITING, TransferStatus._UPLOAD, file_size, 0);
            _user_cb_handler.execute(UserConnectionHandler.TRANSFER_STATUS_CREATED_CB, ts);
            ts.setStatus_Progress(TransferStatus._RUNNING, uploaded_so_far);
            try {
                OutputStream out = _socket.getOutputStream();
                FileInputStream in = new FileInputStream(upload_file);
                while (ull_entry.active() && uploaded_so_far < file_size) {
                    nr_of_chars_read = in.read(byte_arr, 0, sendBufferSize);
                    if (nr_of_chars_read == -1) break;
                    out.write(byte_arr, 0, nr_of_chars_read);
                    uploaded_so_far += nr_of_chars_read;
                    ts.setProgress(uploaded_so_far);
                    _user_cb_handler.execute(UserConnectionHandler.FILE_UPLOAD_PROGRESS_CB, ts);
                }
                if (ull_entry.active()) {
                    _debug("  ... Done!");
                    ts.setStatus_Progress(TransferStatus._FINISHED, uploaded_so_far);
                    _cb_handler.execute(FILE_UPLOAD_DONE_CB, ull_entry);
                } else {
                    _debug("  ... Aborted!");
                    ts.setStatus_Progress(TransferStatus._ABORTED, uploaded_so_far);
                    _cb_handler.execute(FILE_UPLOAD_ABORTED_CB, ull_entry);
                    disconnect();
                }
            } catch (java.io.IOException e) {
                ts.setStatus_Progress(TransferStatus._FAILURE, uploaded_so_far);
                _user_cb_handler.execute(UserConnectionHandler.FILE_UPLOAD_PROGRESS_CB, ts);
                _debug("  ... Failed.");
                libLogger.error(e);
            } finally {
                checkTime cleaner = new checkTime(5, ts, "ts cleaner");
                UploadList.instance().remove(ull_entry);
                _releaseSlot();
            }
        }
    }
