    @Override
    protected void getRemoteChannel(HttpProxyEvent event, final CallBack callback) {
        try {
            if (remoteChannel != null && remoteChannel.isConnected()) {
                callback.callback(remoteChannel);
            }
            if (isHttps) {
                encrypter = new SimpleEncrypter.SimpleEncryptEncoder();
                decrypter = new SimpleEncrypter.SimpleDecryptDecoder();
                seesionId = generateSessionID();
                if (logger.isDebugEnabled()) {
                    logger.debug("Generate https session ID:" + seesionId);
                }
                final URLConnection conn = url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                if (event.getSource() instanceof HttpRequest) {
                    HttpRequest req = (HttpRequest) event.getSource();
                    String target = new String(encrpt.encrypt(getRemoteAddress(req).getBytes()));
                    conn.setRequestProperty("TunnelTarget", target);
                }
                final HttpURLConnection hconn = (HttpURLConnection) conn;
                conn.setRequestProperty("TunnelSource", InetAddress.getLocalHost().getHostAddress() + ":" + tunnelSourcePort);
                conn.setRequestProperty("TunnelSessionId", seesionId);
                PhpTunnelLocalServerHandler.registerCallBack(seesionId, callback);
                final String tempId = seesionId;
                executor.submit(new Runnable() {

                    public void run() {
                        byte[] buf = new byte[4096];
                        try {
                            hconn.connect();
                            if (hconn.getResponseCode() != 200) {
                                close();
                                return;
                            }
                            while (conn.getInputStream().read(buf) > 0) ;
                            close();
                        } catch (Exception e) {
                            logger.error("Failed to communicate with:" + url.toString(), e);
                        } finally {
                            PhpTunnelLocalServerHandler.removeCallBack(tempId);
                        }
                    }
                });
            } else {
                if (event.getSource() instanceof HttpRequest) {
                    String host = url.getHost();
                    int port = url.getPort();
                    if (port == -1) {
                        port = 80;
                    }
                    ChannelPipeline pipeline = pipeline();
                    pipeline.addLast("empty", new EmptyHandler());
                    remoteChannel = factory.newChannel(pipeline);
                    final Channel ch = remoteChannel;
                    remoteChannel.connect(new InetSocketAddress(host, port)).addListener(new ChannelFutureListener() {

                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            callback.callback(ch);
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
