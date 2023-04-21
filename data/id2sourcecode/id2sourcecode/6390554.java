    public void execute() throws Exception {
        switch(getTaskType()) {
            case SoapTasks.TRANSFER_FILES:
                if (_dataUrls == null) {
                    throw new Exception("URLs to data not provided. Aborting.");
                }
                _localFiles = new File[_dataUrls.length];
                String dir = VisbardMain.getRemoteDataDir().getAbsolutePath();
                for (int i = 0; i < _dataUrls.length; i++) {
                    if (_dataUrls[i] == null) throw new MalformedURLException("null URL provided");
                    URL url = new URL(_dataUrls[i]);
                    String fileName = url.getFile().substring(url.getFile().lastIndexOf('/'));
                    _localFiles[i] = new File(dir + fileName);
                    BufferedOutputStream bufOutStream = new BufferedOutputStream(new FileOutputStream(_localFiles[i]));
                    URLConnection URLConn = url.openConnection();
                    BufferedInputStream bufInStream = new BufferedInputStream(URLConn.getInputStream());
                    float fileSize = URLConn.getContentLength();
                    setTaskTitle("Retrieving " + fileName.substring(1) + "  Filesize: " + (int) fileSize);
                    setProgress(i / (float) _dataUrls.length);
                    byte buffer[] = new byte[XFER_BUFFER_SIZE];
                    int len = 0;
                    int offset = 0;
                    while ((len = bufInStream.read(buffer)) > 0) {
                        bufOutStream.write(buffer, 0, len);
                        offset += len;
                        setProgress(offset / fileSize);
                        if (this.isInterrupted()) {
                            bufInStream.close();
                            bufOutStream.close();
                            return;
                        }
                    }
                    bufInStream.close();
                    bufOutStream.close();
                }
                setProgress(1f);
                break;
            default:
                LOG.error("Unknown SoapConnectionTask requested");
                break;
        }
    }
