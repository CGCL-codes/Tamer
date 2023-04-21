    @SuppressWarnings("unchecked")
    public void start(final BundleContext bundleContext) throws Exception {
        log.info("STARTING Java To OSGI Service Converter for " + serviceClass.getName());
        BundleURLScanner scanner = new BundleURLScanner("META-INF/services/", serviceClass.getName(), false);
        BundleObserver<URL> observer = new BundleObserver<URL>() {

            @Override
            public void addingEntries(Bundle bundle, List<URL> urls) {
                log.info(String.format("Bundle %s added with %d factories.", bundle.getSymbolicName(), urls.size()));
                for (URL url : urls) {
                    InputStream inStream = null;
                    String serviceImplementationClassName;
                    try {
                        inStream = url.openStream();
                        serviceImplementationClassName = new BufferedReader(new InputStreamReader(inStream)).readLine();
                        inStream.close();
                    } catch (IOException e) {
                        log.error(e);
                        throw new RuntimeException(e);
                    }
                    log.info(String.format("Instantiating service implementation class: %s", serviceImplementationClassName));
                    try {
                        Class<T> serviceImplementationClass = (Class<T>) bundle.loadClass(serviceImplementationClassName);
                        T serviceImplementation = serviceImplementationClass.newInstance();
                        Dictionary<String, Object> properties = createProperties(serviceImplementation);
                        BundleContext extendedContext = bundle.getBundleContext();
                        extendedContext.registerService(serviceClass.getName(), serviceImplementation, properties);
                        log.info(String.format("Registered %s as a %s service.", serviceImplementationClassName, serviceClass.getName()));
                    } catch (ClassNotFoundException e) {
                        log.error(e);
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        log.error(e);
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        log.error(e);
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void removingEntries(Bundle bundle, List<URL> urls) {
                log.info(String.format("Bundle %s removed with %d services.", bundle.getSymbolicName(), urls.size()));
            }
        };
        BundleWatcher<URL> servicesWatcher = new BundleWatcher<URL>(bundleContext, scanner, observer);
        servicesWatcher.start();
        log.info(String.format("Java To OSGI Services Converter for %s started", serviceClass.getName()));
    }
