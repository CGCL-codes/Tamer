    public boolean Download(Video video, CancelDownload cancelDownload) {
        ServerConfiguration serverConfiguration = Server.getServer().getServerConfiguration();
        GoBackConfiguration goBackConfiguration = Server.getServer().getGoBackConfiguration();
        GetMethod get = null;
        try {
            URL url = new URL(video.getUrl());
            Protocol protocol = new Protocol("https", new TiVoSSLProtocolSocketFactory(), 443);
            HttpClient client = new HttpClient();
            client.getHostConfiguration().setHost(url.getHost(), 443, protocol);
            String password = Tools.decrypt(serverConfiguration.getMediaAccessKey());
            if (video.isParentalControls()) {
                if (serverConfiguration.getPassword() == null) throw new NullPointerException("Parental Controls Password is null");
                password = password + Tools.decrypt(serverConfiguration.getPassword());
            }
            Credentials credentials = new UsernamePasswordCredentials("tivo", password);
            client.getState().setCredentials(null, url.getHost(), credentials);
            get = new GetMethod(video.getUrl());
            client.executeMethod(get);
            if (get.getStatusCode() != 200) {
                log.debug("Status code: " + get.getStatusCode());
                return false;
            }
            InputStream input = get.getResponseBodyAsStream();
            String path = serverConfiguration.getRecordingsPath();
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String name = getFilename(video);
            File file = null;
            if (goBackConfiguration.isGroupByShow()) {
                if (video.getSeriesTitle() != null && video.getSeriesTitle().trim().length() > 0) {
                    path = path + File.separator + clean(video.getSeriesTitle());
                    File filePath = new File(path);
                    if (!filePath.exists()) filePath.mkdirs();
                    file = new File(path + File.separator + name);
                } else file = new File(path + File.separator + name);
            } else {
                file = new File(path + File.separator + name);
            }
            log.info("Downloading: " + name);
            WritableByteChannel channel = new FileOutputStream(file, false).getChannel();
            long total = 0;
            double diff = 0.0;
            ByteBuffer buf = ByteBuffer.allocateDirect(1024 * 4);
            byte[] bytes = new byte[1024 * 4];
            int amount = 0;
            int index = 0;
            long target = video.getSize();
            long start = System.currentTimeMillis();
            long last = start;
            while (amount == 0 && total < target) {
                while (amount >= 0 && !cancelDownload.cancel()) {
                    if (index == amount) {
                        amount = input.read(bytes);
                        index = 0;
                        total = total + amount;
                    }
                    while (index < amount && buf.hasRemaining()) {
                        buf.put(bytes[index++]);
                    }
                    buf.flip();
                    channel.write(buf);
                    if (buf.hasRemaining()) {
                        buf.compact();
                    } else {
                        buf.clear();
                    }
                    if ((System.currentTimeMillis() - last > 10000) && (total > 0)) {
                        try {
                            video = VideoManager.retrieveVideo(video.getId());
                            if (video.getStatus() == Video.STATUS_DOWNLOADING) {
                                diff = (System.currentTimeMillis() - start) / 1000.0;
                                if (diff > 0) {
                                    video.setDownloadSize(total);
                                    video.setDownloadTime((int) diff);
                                    VideoManager.updateVideo(video);
                                }
                            }
                        } catch (HibernateException ex) {
                            log.error("Video update failed", ex);
                        }
                        last = System.currentTimeMillis();
                    }
                }
                if (cancelDownload.cancel()) {
                    channel.close();
                    return false;
                }
            }
            diff = (System.currentTimeMillis() - start) / 1000.0;
            channel.close();
            if (diff != 0) log.info("Download rate=" + (total / 1024) / diff + " KBps");
            try {
                video.setPath(file.getCanonicalPath());
                VideoManager.updateVideo(video);
            } catch (HibernateException ex) {
                log.error("Video update failed", ex);
            }
        } catch (MalformedURLException ex) {
            Tools.logException(ToGo.class, ex, video.getUrl());
            return false;
        } catch (Exception ex) {
            Tools.logException(ToGo.class, ex, video.getUrl());
            return false;
        } finally {
            if (get != null) get.releaseConnection();
        }
        return true;
    }
