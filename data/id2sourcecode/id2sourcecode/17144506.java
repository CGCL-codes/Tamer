    public FoxResponse processResponse(FoxRequest pFoxRequest) {
        if (pFoxRequest.getCookieValue("streamsession") == null || !pFoxRequest.getCookieValue("streamsession").equals(mSessionCookie)) {
            throw new ExInternal("Cannot serve links to users who did not generate the content");
        }
        FoxResponseByteStream lFoxResponse = null;
        int lFileIndex = 0;
        String lMode = XFUtil.nvl(pFoxRequest.getHttpRequest().getParameter("mode"), "save");
        boolean lIsAttachment = false;
        String lAttach = pFoxRequest.getHttpRequest().getParameter("attach");
        if (!XFUtil.isNull(lAttach) && lAttach.equals("true")) {
            lIsAttachment = true;
        }
        if (mType == TYPE_STANDARD) {
            if (mStreamParcelInputList.size() == 0) {
                throw new ExInternal("There are no documents in this stream parcel");
            } else if (mStreamParcelInputList.size() > 1) {
                String lfileIndexParam = pFoxRequest.getHttpRequest().getParameter("i");
                if (XFUtil.isNull(lfileIndexParam)) {
                    throw new ExInternal("This stream parcel has multiple documents and no file id passed in url");
                }
                try {
                    lFileIndex = Integer.parseInt(lfileIndexParam) - 1;
                } catch (NumberFormatException e) {
                    throw new ExInternal("The file id requested is not valid id.", e);
                }
            }
            StreamParcelInput lInputStreamFile = (StreamParcelInput) mStreamParcelInputList.get(lFileIndex);
            UCon lUcon = null;
            if (lInputStreamFile instanceof StreamParcelInputTempResource || lInputStreamFile instanceof StreamParcelInputFileUpload) {
                try {
                    lUcon = UCon.open(mApp.mConnectKey, "StreamParcel.processResponse");
                    lUcon.setClientInfo("StreamParcel");
                } catch (ExServiceUnavailable e) {
                    throw new ExInternal("Error getting database connection for StreamParcel response", e);
                }
            }
            XThread lXThread = null;
            if (lInputStreamFile instanceof StreamParcelInputFileUpload) {
                String lThreadID = pFoxRequest.getHttpRequest().getParameter("t");
                String lAppMnem = pFoxRequest.getHttpRequest().getParameter("a");
                try {
                    lXThread = XThread.reloadThread(lAppMnem, lThreadID, pFoxRequest);
                } catch (ExModule e) {
                    throw new ExInternal("Problem starting thread for !STREAM request", e);
                } catch (ExServiceUnavailable e) {
                    throw new ExInternal("Problem starting thread for !STREAM request", e);
                }
                try {
                    lXThread.alterThreadSetLevel(pFoxRequest, XThread.THREAD_MOUNTED, lUcon);
                } catch (Exception e) {
                    throw new ExInternal("Error mounting thread for StreamParcel", e);
                }
            }
            if (lInputStreamFile instanceof StreamParcelInputTempResource) {
                StreamParcelInputTempResource lInputStreamFileTempResource = (StreamParcelInputTempResource) lInputStreamFile;
                lInputStreamFileTempResource.setParams(lUcon, mApp);
            } else if (lInputStreamFile instanceof StreamParcelInputFileUpload) {
                StreamParcelInputFileUpload lInputStreamFileUpload = (StreamParcelInputFileUpload) lInputStreamFile;
                lInputStreamFileUpload.setParams(lXThread);
            }
            lFoxResponse = new FoxResponseByteStream(lInputStreamFile.getFileType(), pFoxRequest, 0);
            OutputStream lFileOutputStream = null;
            try {
                if (lMode.equals("save")) {
                    lFoxResponse.setHttpHeader("Content-Disposition", "attachment; filename=\"" + lInputStreamFile.getFileName() + "\"");
                } else {
                    lFoxResponse.setHttpHeader("Content-Disposition", "filename=\"" + lInputStreamFile.getFileName() + "\"");
                }
                String lContentType = lInputStreamFile.getFileType();
                if (!XFUtil.isNull(lContentType)) {
                    lFoxResponse.setHttpHeader("Content-Type", lContentType);
                }
                long lContentLength = lInputStreamFile.getSize();
                if (lContentLength > 0) {
                    lFoxResponse.setHttpHeader("Content-Length", Long.toString(lContentLength));
                }
                lFileOutputStream = lFoxResponse.getHttpServletOutputStream();
                streamDataFromStreamParcelInput(lInputStreamFile, lFileOutputStream, lXThread, lUcon, pFoxRequest);
            } catch (Throwable th) {
                if (lXThread != null) {
                    lXThread.abort(pFoxRequest);
                }
            } finally {
                try {
                    if (lFileOutputStream != null) {
                        lFileOutputStream.close();
                    }
                } catch (Throwable th) {
                }
                if (lUcon != null) {
                    try {
                        lUcon.rollback();
                    } catch (Throwable th) {
                        int i = 1;
                    }
                    lUcon.closeForRecycle();
                }
            }
        } else if (mType == TYPE_ZIP_FILE) {
            HashMap lFileNameList = new HashMap();
            lFoxResponse = new FoxResponseByteStream("application/zip", pFoxRequest, 0);
            lFoxResponse.setHttpHeader("Content-Disposition", "attachment; filename=\"" + mFileName + "\"");
            try {
                ZipOutputStream lZipOutputStream = new ZipOutputStream(lFoxResponse.getHttpServletOutputStream());
                lZipOutputStream.setLevel(mZipCompressionLevel);
                int lBytesCopied = 0;
                for (int i = 0; i < mStreamParcelInputList.size(); i++) {
                    StreamParcelInput lInputStreamFile = (StreamParcelInput) mStreamParcelInputList.get(i);
                    String lPathFile = lInputStreamFile.getPath() + lInputStreamFile.getFileName();
                    Integer lcount = (Integer) lFileNameList.get(lPathFile);
                    if (lcount == null) {
                        lFileNameList.put(lPathFile, new Integer(1));
                    } else {
                        lFileNameList.put(lPathFile, new Integer(lcount.intValue() + 1));
                        int lExtIndex = lPathFile.lastIndexOf(".");
                        String lExt = "";
                        String lFileName = "";
                        if (lExtIndex >= 0) {
                            lExt = lPathFile.substring(lExtIndex);
                            lFileName = lPathFile.substring(0, lExtIndex);
                        } else {
                            lFileName = lPathFile;
                        }
                        lPathFile = lFileName + "(" + new Integer(lcount.intValue() + 1) + ")" + lExt;
                    }
                    try {
                        ZipEntry lZipFileEntry = new ZipEntry(lPathFile);
                        lZipOutputStream.putNextEntry(lZipFileEntry);
                        streamDataFromStreamParcelInput(lInputStreamFile, lZipOutputStream, null, null, pFoxRequest);
                        lZipOutputStream.closeEntry();
                    } catch (IOException e) {
                        throw new ExInternal("Error writing ZIP file entry from StreamParcelInput", e);
                    } catch (Exception e) {
                        throw new ExInternal("Error writing ZIP file entry from StreamParcelInput", e);
                    }
                }
                lZipOutputStream.close();
            } catch (IOException e) {
                throw new ExInternal("Error closing ZIP", e);
            }
        }
        return lFoxResponse;
    }
