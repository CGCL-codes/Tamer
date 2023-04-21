    private void handleRequest(String requestUrl) {
        Log.i(LOG_CATEGORY, requestUrl);
        httpGet = new HttpGet(requestUrl);
        if (!httpGet.isAborted()) {
            SecurityUtil.addCredentialToHttpRequest(context, httpGet);
            try {
                HttpResponse response = client.execute(httpGet);
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == Constants.HTTP_SUCCESS) {
                    PollingStatusParser.parse(response.getEntity().getContent());
                } else {
                    response.getEntity().getContent().close();
                    handleServerErrorWithStatusCode(statusCode);
                }
                return;
            } catch (SocketTimeoutException e) {
                Log.i(LOG_CATEGORY, "polling [" + pollingStatusIds + "] socket timeout.");
            } catch (ClientProtocolException e) {
                isPolling = false;
                Log.e(LOG_CATEGORY, "polling [" + pollingStatusIds + "] failed.", e);
                handler.sendEmptyMessage(NETWORK_ERROR);
            } catch (SocketException e) {
                if (isPolling) {
                    isPolling = false;
                    Log.e(LOG_CATEGORY, "polling [" + pollingStatusIds + "] failed.", e);
                    handler.sendEmptyMessage(NETWORK_ERROR);
                }
            } catch (IllegalArgumentException e) {
                isPolling = false;
                Log.e(LOG_CATEGORY, "polling [" + pollingStatusIds + "] failed", e);
                handler.sendEmptyMessage(NETWORK_ERROR);
            } catch (OutOfMemoryError e) {
                isPolling = false;
                Log.e(LOG_CATEGORY, "OutOfMemoryError");
            } catch (InterruptedIOException e) {
                isPolling = false;
                Log.i(LOG_CATEGORY, "last polling [" + pollingStatusIds + "] has been shut down");
            } catch (IOException e) {
                isPolling = false;
                Log.i(LOG_CATEGORY, "last polling [" + pollingStatusIds + "] already aborted");
            }
        }
    }
