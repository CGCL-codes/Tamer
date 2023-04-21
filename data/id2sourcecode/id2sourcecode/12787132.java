        boolean download(Notification notification) {
            System.setProperty("httpclient.useragent", getString(R.string.app_user_agent_string));
            HttpClient client = new HttpClient();
            client.getParams().setParameter("http.socket.timeout", new Integer(10000));
            GetMethod method = new GetMethod(feed.getUri().toString());
            Log.i("AFR", "FeedRetrieverService.retriever.download(): retrieving feed from: " + feed.getUri().toString());
            try {
                method.setFollowRedirects(true);
                method.addRequestHeader("Accept-Encoding", "gzip");
                if (feed.getEtag() != null) {
                    method.addRequestHeader("If-None-Match", feed.getEtag());
                }
                if (feed.getLastModified() != null) {
                    method.addRequestHeader("If-Modified-Since", feed.getLastModified());
                }
                if (!handleHttpStatus(client.executeMethod(method))) {
                    return false;
                }
                Header header = method.getResponseHeader("Last-Modified");
                if (header != null) {
                    feed.setLastModified(header.getValue());
                }
                header = method.getResponseHeader("ETag");
                if (header != null) {
                    feed.setEtag(header.getValue());
                }
                header = method.getResponseHeader("Content-Type");
                if (header != null) {
                    contentType = header.getValue();
                }
                InputStream in = null;
                if (method.getResponseHeader("Content-Encoding") != null && "gzip".equalsIgnoreCase(method.getResponseHeader("Content-Encoding").getValue())) {
                    in = new GZIPInputStream(method.getResponseBodyAsStream());
                } else {
                    in = method.getResponseBodyAsStream();
                }
                file = File.createTempFile("afr-feed-", ".xml");
                FileOutputStream out = new FileOutputStream(file);
                long length = method.getResponseContentLength();
                if (length != -1) {
                    String details = getText(R.string.feed_retriever_notification_dl).toString();
                    Log.i("AFR", "FeedRetrieverService.retriever.download(): Content-Length = " + length);
                    byte[] buffer = new byte[1024];
                    float total = (float) length, readSoFar = 0f;
                    for (int read = in.read(buffer); read != -1; read = in.read(buffer)) {
                        out.write(buffer, 0, read);
                        readSoFar += read;
                        notification.statusBarBalloonText = String.format(details, feed.getName() != null ? feed.getName() : feed.getUri().toString(), readSoFar / total * 100f).toString();
                    }
                } else {
                    Log.i("AFR", "FeedRetrieverService.retriever.download(): Content-Length not given");
                    byte[] buffer = new byte[1024 * 8];
                    for (int read = in.read(buffer); read != -1; read = in.read(buffer)) {
                        out.write(buffer, 0, read);
                    }
                }
                in.close();
                out.close();
            } catch (UnknownHostException e) {
                Log.e("AFR", "FeedRetrieverService.retriever.download(): failed to retrieve feed: host not found", e);
                String detail = feed.getUri().getScheme() + "://" + feed.getUri().getHost();
                displayErrorNotification(R.string.feed_retriever_err_unknown_host, detail);
                return false;
            } catch (SocketTimeoutException e) {
                Log.e("AFR", "FeedRetrieverService.retriever.download(): failed to retrieve feed: timed out", e);
                String detail = feed.getUri().getScheme() + "://" + feed.getUri().getHost();
                displayErrorNotification(R.string.feed_retriever_err_timed_out, detail);
                return false;
            } catch (IOException e) {
                Log.e("AFR", "FeedRetrieverService.retriever.download(): failed to retrieve feed: I/O error", e);
                displayErrorNotification(R.string.feed_retriever_err_unspecified, feed.getName() != null ? feed.getName() : feed.getUri().toString());
                return false;
            } finally {
                method.releaseConnection();
            }
            return true;
        }
