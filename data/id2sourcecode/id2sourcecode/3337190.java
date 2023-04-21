    protected void getAttachment(PartRequest req) throws IOException {
        Attachment att = req.att;
        Message msg = Message.restoreMessageWithId(mContext, att.mMessageKey);
        doProgressCallback(msg.mId, att.mId, 0);
        String cmd = "GetAttachment&AttachmentName=" + att.mLocation;
        HttpResponse res = sendHttpClientPost(cmd, null, COMMAND_TIMEOUT);
        int status = res.getStatusLine().getStatusCode();
        if (status == HttpStatus.SC_OK) {
            HttpEntity e = res.getEntity();
            int len = (int) e.getContentLength();
            InputStream is = res.getEntity().getContent();
            File f = (req.destination != null) ? new File(req.destination) : createUniqueFileInternal(req.destination, att.mFileName);
            if (f != null) {
                File destDir = f.getParentFile();
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }
                FileOutputStream os = new FileOutputStream(f);
                if (len != 0) {
                    try {
                        mPendingPartRequest = req;
                        byte[] bytes = new byte[CHUNK_SIZE];
                        int length = len;
                        int totalRead = 0;
                        userLog("Attachment content-length: ", len);
                        while (true) {
                            int read = is.read(bytes, 0, CHUNK_SIZE);
                            if (read < 0) {
                                userLog("Attachment load reached EOF, totalRead: ", totalRead);
                                break;
                            }
                            totalRead += read;
                            os.write(bytes, 0, read);
                            if (length > 0) {
                                if (totalRead > length) {
                                    errorLog("totalRead is greater than attachment length?");
                                    break;
                                }
                                int pct = (totalRead * 100 / length);
                                doProgressCallback(msg.mId, att.mId, pct);
                            }
                        }
                    } finally {
                        mPendingPartRequest = null;
                    }
                }
                os.flush();
                os.close();
                if (att.isSaved()) {
                    String contentUriString = (req.contentUriString != null) ? req.contentUriString : "file://" + f.getAbsolutePath();
                    ContentValues cv = new ContentValues();
                    cv.put(AttachmentColumns.CONTENT_URI, contentUriString);
                    att.update(mContext, cv);
                    doStatusCallback(msg.mId, att.mId, EmailServiceStatus.SUCCESS);
                }
            }
        } else {
            doStatusCallback(msg.mId, att.mId, EmailServiceStatus.MESSAGE_NOT_FOUND);
        }
    }
