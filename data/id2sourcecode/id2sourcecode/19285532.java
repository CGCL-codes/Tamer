    public void traverse(final String url, final StageHolder stageHolder, final ConnectionListener listener) throws LinkTraverserException {
        try {
            listener.updateStatus(stageHolder.getState(), "Connecting to url -> " + url);
            stageHolder.nextState();
            HttpResponse response = openConnection(httpClient, url);
            listener.handleEvent(httpClient, response);
            listener.updateStatus(stageHolder.getState(), "closing connection to url -> " + url);
            closeConnection(response);
        } catch (Exception e) {
            String error = "There was an error connecting to url -> " + url;
            listener.onError(stageHolder.getState(), error, e);
            throw new LinkTraverserException(error, e);
        }
    }
