    @Override
    public void handle(Request request, Response response) {
        try {
            if (Protocol.FTP.equals(request.getProtocol())) {
                if (Method.GET.equals(request.getMethod())) {
                    Reference ftpRef = request.getResourceRef();
                    String userInfo = null;
                    if ((request.getChallengeResponse() != null) && ChallengeScheme.FTP_PLAIN.equals(request.getChallengeResponse().getScheme()) && (request.getChallengeResponse().getIdentifier() != null)) {
                        userInfo = request.getChallengeResponse().getIdentifier();
                        if (request.getChallengeResponse().getSecret() != null) {
                            userInfo += ":" + new String(request.getChallengeResponse().getSecret());
                        }
                    }
                    if (userInfo != null) {
                        ftpRef.setUserInfo(userInfo);
                    }
                    URL url = ftpRef.toUrl();
                    URLConnection connection = url.openConnection();
                    int majorVersionNumber = SystemUtils.getJavaMajorVersion();
                    int minorVersionNumber = SystemUtils.getJavaMinorVersion();
                    if ((majorVersionNumber > 1) || ((majorVersionNumber == 1) && (minorVersionNumber >= 5))) {
                        connection.setConnectTimeout(getConnectTimeout());
                        connection.setReadTimeout(getReadTimeout());
                    }
                    connection.setAllowUserInteraction(isAllowUserInteraction());
                    connection.setUseCaches(isUseCaches());
                    response.setEntity(new InputRepresentation(connection.getInputStream()));
                    Entity.updateMetadata(request.getResourceRef().getPath(), response.getEntity(), true, getMetadataService());
                } else {
                    getLogger().log(Level.WARNING, "Only GET method are supported by this FTP connector");
                }
            }
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "FTP client error", e);
            response.setStatus(Status.CONNECTOR_ERROR_INTERNAL, e.getMessage());
        }
    }
