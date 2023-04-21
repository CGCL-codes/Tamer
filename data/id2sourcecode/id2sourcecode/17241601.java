    public static void executeRequest(final HttpRequestBase request, final ResultHandler rh) {
        Handler handler = rh.getHandler();
        Context ctx = rh.getContext();
        SharedPreferences prefs = ctx.getSharedPreferences(Andolphin.Prefs.ANDOLPHIN, Context.MODE_WORLD_READABLE);
        String username = prefs.getString(Andolphin.Prefs.USERNAME, "");
        String authToken = prefs.getString(Andolphin.Prefs.AUTH_TOKEN, "");
        request.setHeader(USERNAME, username);
        request.setHeader(AUTH_TOKEN, authToken);
        DefaultHttpClient client = new DefaultHttpClient();
        Message message = handler.obtainMessage();
        Bundle bundle = new Bundle();
        try {
            HttpResponse response = client.execute(request);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                String resultText = StringUtils.readStringFromStream(entity.getContent());
                try {
                    JSONObject json = new JSONObject(resultText);
                    Result result = Result.createFromJSONObject(json);
                    sendMessage(handler, message, bundle, result);
                } catch (JSONException e) {
                    Log.e(TAG, StringUtils.digMessage(e), e);
                    Result result = Result.DATA_ERROR;
                    result.setMessage(StringUtils.digMessage(e));
                    sendMessage(handler, message, bundle, result);
                }
            } else {
                Log.e(TAG, "Http Response StatusCode : " + status.getStatusCode());
                sendMessage(handler, message, bundle, Result.NETWORK_ERROR);
            }
        } catch (ClientProtocolException e) {
            Log.e(TAG, StringUtils.digMessage(e), e);
            sendMessage(handler, message, bundle, Result.NETWORK_ERROR);
        } catch (IOException e) {
            Log.e(TAG, StringUtils.digMessage(e), e);
            sendMessage(handler, message, bundle, Result.NETWORK_ERROR);
        } catch (Exception e) {
            Log.e(TAG, StringUtils.digMessage(e), e);
            sendMessage(handler, message, bundle, Result.UNKNOWN_ERROR);
        }
    }
