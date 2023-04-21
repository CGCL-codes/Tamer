        @Override
        public void download(DownloadListener listener, InputStreamFilterChain filterChain) throws DownloadServiceException {
            try {
                final HttpGet request = new HttpGet(url.toString());
                final HttpResponse response = client.execute(request);
                final String content = IOUtil.toString(response.getEntity().getContent());
                final String stringTimer = RegexpUtils.find(DOWNLOAD_TIMER, content, 1);
                int timer = 0;
                if (stringTimer != null && stringTimer.length() > 0) {
                    timer = Integer.parseInt(stringTimer);
                }
                if (timer > 0 && configuration.respectWaitTime()) {
                    listener.timer(timer * 1000, TimerWaitReason.DOWNLOAD_TIMER);
                    ThreadUtils.sleep(timer * 1000);
                }
                final String downloadUrl = RegexpUtils.find(DOWNLOAD_DIRECT_LINK_PATTERN, content, 0);
                if (downloadUrl != null && downloadUrl.length() > 0) {
                    final HttpGet downloadRequest = new HttpGet(downloadUrl);
                    final HttpResponse downloadResponse = client.execute(downloadRequest);
                    if (downloadResponse.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
                        listener.timer(10 * 60 * 1000, TimerWaitReason.COOLDOWN);
                        ThreadUtils.sleep(10 * 60 * 1000);
                        download(listener, filterChain);
                    } else {
                    }
                } else {
                    throw new DownloadServiceException("Download link not found");
                }
            } catch (IOException e) {
                throw new DownloadServiceException(e);
            }
        }
