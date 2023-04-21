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
