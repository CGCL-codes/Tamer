    public void deleteServerWorkspace() throws URISyntaxException, ClientProtocolException, IOException {
        SPServerInfo serviceInfo = workspaceLocation.getServiceInfo();
        HttpClient httpClient = createHttpClient(serviceInfo);
        try {
            HttpUriRequest request = new HttpDelete(getServerURI(serviceInfo, "workspaces/" + getWorkspace().getUUID()));
            httpClient.execute(request, new HttpResponseHandler());
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
