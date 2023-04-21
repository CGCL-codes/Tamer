    protected void initialize(LogNamespace namespace, boolean isTailingReceiver) throws ReceiverInitializationException {
        this.logNamespace = namespace;
        this.isTailing = isTailingReceiver;
        logInterpreter = new LogInterpreter(namespace);
        String url = namespace.getLocalSourceString();
        url = url.replaceAll("#", "%23");
        try {
            reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        } catch (IOException ioe) {
            throw new ReceiverInitializationException("Unable to load : " + (url == null ? null : url.toString()), ioe);
        }
    }
