    public void submit(final String url, final ConnectionListener listener, final StageHolder stageHolder, final NameValuePairBuilder nameValuePairBuilder) {
        try {
            listener.updateStatus(stageHolder.getState(), "Connecting to url -> " + url);
            stageHolder.nextState();
            HttpPost httpost = openConnection(url, nameValuePairBuilder);
            listener.updateStatus(stageHolder.getState(), "Submitting form");
            stageHolder.nextState();
            HttpResponse response = httpClient.execute(httpost);
            listener.handleEvent(httpClient, response);
            listener.updateStatus(stageHolder.getState(), "Closing connecting to url -> " + url);
            closeConnection(response);
        } catch (Exception e) {
            List<NameValuePair> nameValuePairList = nameValuePairBuilder.build();
            String error = "Could not submit form to url -> " + url + ", with properties -> " + nameValuePairList;
            listener.onError(stageHolder.getState(), error, e);
            throw new FormSubmitterException(error, e);
        }
    }
