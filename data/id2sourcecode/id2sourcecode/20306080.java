    public Service registerService(ProtocolHandlerFactory factory, ServerSocketConfiguration ssconf) throws IOException {
        ServerSocket ss = ssconf.createSocket();
        ServerSocketChannel ssc = ss.getChannel();
        Service service = new Service(ss.getLocalSocketAddress(), factory, this);
        acceptSelectors.register(ssc, service);
        synchronized (services) {
            services.put(service, ss);
        }
        return service;
    }
