    @Override
    public void start() throws DownloadException {
        try {
            if (getFile().exists()) if (getFile().isFile()) this.append = true; else return; else getFile().createNewFile();
            if (isDownloading() == false) setDownloading(true);
            this.connection = (HttpURLConnection) url.openConnection();
            this.connection.setRequestProperty("Range", "bytes=" + getFile().length() + "-");
            this.connection.connect();
            setContentType(this.connection.getContentType());
            setSize(this.connection.getContentLength());
            if (getSize() == -1) setResumable(false); else setResumable(true);
            if (getSize() <= getFile().length() && getSize() != -1) {
                setDownloading(false);
                return;
            } else {
                new Thread(this).start();
            }
        } catch (IOException ex) {
            throw new DownloadException(ex.getMessage());
        } finally {
            speedTimer.cancel();
        }
    }
