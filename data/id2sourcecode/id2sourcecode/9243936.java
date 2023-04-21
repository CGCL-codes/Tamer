    private URLConnection obtainConnection(URL urlForLoading) throws IOException {
        Proxy proxy = getProxy();
        if (proxy == null || proxy == Proxy.NO_PROXY) return urlForLoading.openConnection(); else return urlForLoading.openConnection(proxy);
    }
