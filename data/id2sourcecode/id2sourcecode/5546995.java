    @Override
    public void run() {
        try {
            Proxy proxy;
            if (!config.sidplay2().isEnableProxy()) {
                proxy = null;
            } else {
                final SocketAddress addr = new InetSocketAddress(config.sidplay2().getProxyHostname(), config.sidplay2().getProxyPort());
                proxy = new Proxy(Proxy.Type.HTTP, addr);
            }
            URL url = new URL(fURL);
            URLConnection conn;
            if (proxy != null) {
                conn = url.openConnection(proxy);
            } else {
                conn = url.openConnection();
            }
            conn.connect();
            InputStream is;
            try {
                is = conn.getInputStream();
            } catch (FileNotFoundException e) {
                fListener.downloadStop(null);
                return;
            }
            File downloadedFile = new File(System.getProperty("jsidplay2.tmpdir"), new File(url.getPath()).getName());
            downloadedFile.deleteOnExit();
            FileOutputStream out = new FileOutputStream(downloadedFile);
            double total = 0;
            int bytesRead;
            byte[] buffer = new byte[MAX_BUFFER_SIZE];
            while ((bytesRead = is.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                total += bytesRead;
                if (isAborted) {
                    fListener.downloadStop(null);
                    out.close();
                    is.close();
                    return;
                }
                fListener.downloadStep((int) ((total / conn.getContentLength()) * 100L));
            }
            out.close();
            is.close();
            fListener.downloadStop(downloadedFile);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            fListener.downloadStop(null);
        } catch (IOException e1) {
            e1.printStackTrace();
            fListener.downloadStop(null);
        }
    }
