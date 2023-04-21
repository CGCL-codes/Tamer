    @Override
    public void run() {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            if (useProxy) {
                Properties systemProperties = System.getProperties();
                systemProperties.setProperty("ftp.proxyHost", proxyUrl);
                systemProperties.setProperty("ftp.proxyPort", String.valueOf(port));
                if (useAuthentication) {
                    Authenticator.setDefault(new SimpleAuthenticator(userName, password));
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append(path);
            if (!path.endsWith(System.getProperty("file.separator"))) {
                sb.append(System.getProperty("file.separator"));
            }
            sb.append(getFileName(url));
            status = DOWNLOADING;
            URLConnection urlc = url.openConnection();
            bis = new BufferedInputStream(urlc.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(sb.toString()));
            while (status == DOWNLOADING) {
                byte[] buffer = new byte[MAX_BUFFER_SIZE];
                int read = bis.read(buffer);
                if (read == -1) {
                    break;
                }
                bos.write(buffer, 0, read);
                downloaded += read;
            }
            status = COMPLETE;
            stateChanged();
        } catch (MalformedURLException e) {
            ErrorHandler.getOne(this.getClass(), debug).handle(e);
            setDebugMessage(e.toString(), true);
            error();
        } catch (IOException e) {
            ErrorHandler.getOne(this.getClass(), debug).handle(e);
            setDebugMessage(e.toString(), true);
            error();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ioe) {
                    ErrorHandler.getOne(this.getClass(), debug).handle(ioe);
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException ioe) {
                    ErrorHandler.getOne(this.getClass(), debug).handle(ioe);
                }
            }
        }
    }
