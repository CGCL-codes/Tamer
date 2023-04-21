    protected void internalConnect() throws IOException {
        if (proxy == null) urlConnection = (HttpURLConnection) urlObject.openConnection(); else urlConnection = (HttpURLConnection) urlObject.openConnection(proxy);
        urlConnection.setDoOutput(true);
        setHttpRequestHeaders();
        serializationContext.instantiateTypes = instantiateTypes;
        actionContext = new ActionContext();
        connected = true;
    }
