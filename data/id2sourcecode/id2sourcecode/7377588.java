    public void execute() {
        httpStatus = 0;
        timeMs = 0;
        URL url = null;
        CoreEvent event = null;
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try {
            url = new URL(resource.getUrl());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");
            if (!SpiderUtil.isStringNull(controller.getCookie())) {
                connection.addRequestProperty("Cookie", controller.getCookie());
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727)");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setAllowUserInteraction(false);
                connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");
                connection.setRequestProperty("Accept", "*/*");
                connection.setRequestProperty("accept-language", "zh-cn");
                connection.setRequestProperty("Cache-Control", "no-cache");
                connection.setRequestProperty("Pragma", "no-cache");
                connection.setRequestProperty("Host", controller.getContent().getBaseUrl());
                connection.setRequestProperty("Connection", "keep-alive");
                connection.setAllowUserInteraction(false);
            }
            long start = System.currentTimeMillis();
            connection.setConnectTimeout(90 * 1000);
            connection.setReadTimeout(90 * 1000);
            connection.connect();
            httpStatus = connection.getResponseCode();
            inputStream = new BufferedInputStream(connection.getInputStream());
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            InputStream is = new BufferedInputStream(inputStream);
            try {
                byte bytes[] = new byte[40960];
                int nRead = -1;
                while ((nRead = is.read(bytes, 0, 40960)) > 0) {
                    os.write(bytes, 0, nRead);
                }
                os.close();
                is.close();
                inputStream.close();
            } catch (SocketTimeoutException e) {
                log.error("Read timed out url [" + url + "]");
                os.close();
                is.close();
                inputStream.close();
            } catch (IOException e) {
                log.error("SpiderHttpURLTask execute IOException url [" + url + "] - >", e);
                event = new URLSpideredErrorEvent(controller, resource, 14);
                os.close();
                is.close();
                inputStream.close();
            }
            String contentType = connection.getContentType();
            int timeMs = (int) (System.currentTimeMillis() - start) / 1000;
            if (httpStatus >= 200 && httpStatus <= 301) {
                event = new URLSpideredOkEvent(controller, resource, httpStatus, contentType, os.toByteArray());
            } else {
                event = new URLSpideredErrorEvent(controller, resource, httpStatus);
                log.error("SpiderHttpURLTask " + this.toString());
                controller.cheat();
            }
            log.info("connect ... " + this + " Time [" + timeMs + "]");
        } catch (FileNotFoundException e) {
            event = new URLSpideredErrorEvent(controller, resource, 40);
            log.error("SpiderHttpURLTask FileNotFountException - ��ҳ�治����  - " + this.toString());
            controller.cheat();
        } catch (Exception e) {
            event = new URLSpideredErrorEvent(controller, resource, 10);
            log.error("SpiderHttpURLTask Exception " + this.toString(), e);
            controller.cheat();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("SpiderHttpURLTask IOException");
                }
            }
            this.notifyEvent(resource, event);
        }
    }
