    public void parseTextBlock(InputStream dumpStream, long contentLength, boolean isDelta) throws SVNException {
        if (isDelta) {
            applyTextDelta();
        } else {
            setFullText();
        }
        byte[] buffer = null;
        if (contentLength > 0) {
            buffer = new byte[SVNFileUtil.STREAM_CHUNK_SIZE];
            while (contentLength > 0) {
                int numToRead = contentLength > SVNFileUtil.STREAM_CHUNK_SIZE ? SVNFileUtil.STREAM_CHUNK_SIZE : (int) contentLength;
                int read = 0;
                while (numToRead > 0) {
                    int numRead = -1;
                    try {
                        numRead = dumpStream.read(buffer, read, numToRead);
                    } catch (IOException ioe) {
                        SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.IO_ERROR, ioe.getMessage());
                        SVNErrorManager.error(err, ioe, SVNLogType.FSFS);
                    }
                    if (numRead < 0) {
                        SVNAdminHelper.generateIncompleteDataError();
                    }
                    read += numRead;
                    numToRead -= numRead;
                }
                if (!myCurrentNodeBaton.myIsDoSkip) {
                    try {
                        myOutputStream.write(buffer, 0, read);
                    } catch (IOException ioe) {
                        SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.STREAM_UNEXPECTED_EOF, "Unexpected EOF writing contents");
                        SVNErrorManager.error(err, ioe, SVNLogType.FSFS);
                    }
                }
                contentLength -= read;
            }
        }
    }
