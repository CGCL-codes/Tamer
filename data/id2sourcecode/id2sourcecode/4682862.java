    public RuleExecutionSet createRuleExecutionSet(String uri, Map properties) throws RuleExecutionSetCreateException, IOException, RemoteException {
        URLConnection urlc = (new URL(uri)).openConnection();
        java.io.InputStream is = urlc.getInputStream();
        return (new LocalRuleExecutionSetProviderImpl()).createRuleExecutionSet(is, properties);
    }
