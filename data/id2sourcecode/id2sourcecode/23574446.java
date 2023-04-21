    protected void getContentIntoAStreamInternal(OutputStream os, Object obj, boolean isRetrieve, int handle, int pos) throws XMLDBException {
        if (vfile != null || contentVFile != null || inputSource != null || obj != null) {
            InputStream bis = null;
            try {
                if (vfile != null) {
                    bis = vfile.getByteStream();
                } else if (inputSource != null) {
                    bis = inputSource.getByteStream();
                } else if (obj != null) {
                    bis = getAnyStream(obj);
                } else {
                    bis = contentVFile.getByteStream();
                }
                int readed;
                byte buffer[] = new byte[65536];
                while ((readed = bis.read(buffer)) != -1) {
                    os.write(buffer, 0, readed);
                }
            } catch (IOException ioe) {
                throw new XMLDBException(ErrorCodes.VENDOR_ERROR, ioe.getMessage(), ioe);
            } finally {
                if (inputSource != null) {
                    if (bis != null) {
                        if (bis.markSupported()) {
                            try {
                                bis.reset();
                            } catch (IOException ioe) {
                            }
                        }
                    }
                } else {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException ioe) {
                        }
                    }
                }
            }
        } else {
            getRemoteContentIntoLocalFile(os, isRetrieve, handle, pos);
        }
    }
