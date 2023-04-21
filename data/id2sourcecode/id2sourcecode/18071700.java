    private ProxyAutoConfiguration init(URI pacFileUri) throws IOException {
        this.pacFileUri = pacFileUri;
        jsContext = ContextFactory.getGlobal().enterContext();
        scope = jsContext.initStandardObjects();
        scope.defineFunctionProperties(new String[] { "isPlainHostName", "dnsDomainIs", "localHostOrDomainIs", "isResolvable", "isInNet", "dnsResolve", "myIpAddress", "dnsDomainLevels", "shExpMatch", "weekdayRange", "dateRange", "timeRange" }, ProxyAutoConfiguration.class, ScriptableObject.DONTENUM);
        URL url = pacFileUri.toURL();
        InputStream bytesStream = url.openStream();
        Reader charStream = new InputStreamReader(bytesStream);
        jsContext.evaluateReader(scope, charStream, pacFileUri.toString(), 1, null);
        findProxyForURLFunction = (Function) scope.get("FindProxyForURL", scope);
        return this;
    }
