    protected void feedMetaInfServicesFactories() {
        try {
            for (String factoryName : FACTORY_NAMES) {
                Iterator<URL> it = ClassUtils.getResources(META_INF_SERVICES_RESOURCE_PREFIX + factoryName, this);
                while (it.hasNext()) {
                    URL url = it.next();
                    InputStream stream = openStreamWithoutCache(url);
                    InputStreamReader isr = new InputStreamReader(stream);
                    BufferedReader br = new BufferedReader(isr);
                    String className;
                    try {
                        className = br.readLine();
                    } catch (IOException e) {
                        throw new FacesException("Unable to read class name from file " + url.toExternalForm(), e);
                    } finally {
                        if (br != null) {
                            br.close();
                        }
                        if (isr != null) {
                            isr.close();
                        }
                        if (stream != null) {
                            stream.close();
                        }
                    }
                    if (log.isLoggable(Level.INFO)) {
                        log.info("Found " + factoryName + " factory implementation: " + className);
                    }
                    if (factoryName.equals(FactoryFinder.APPLICATION_FACTORY)) {
                        getDispenser().feedApplicationFactory(className);
                    } else if (factoryName.equals(FactoryFinder.EXTERNAL_CONTEXT_FACTORY)) {
                        getDispenser().feedExternalContextFactory(className);
                    } else if (factoryName.equals(FactoryFinder.FACES_CONTEXT_FACTORY)) {
                        getDispenser().feedFacesContextFactory(className);
                    } else if (factoryName.equals(FactoryFinder.LIFECYCLE_FACTORY)) {
                        getDispenser().feedLifecycleFactory(className);
                    } else if (factoryName.equals(FactoryFinder.RENDER_KIT_FACTORY)) {
                        getDispenser().feedRenderKitFactory(className);
                    } else if (factoryName.equals(FactoryFinder.PARTIAL_VIEW_CONTEXT_FACTORY)) {
                        getDispenser().feedPartialViewContextFactory(className);
                    } else if (factoryName.equals(FactoryFinder.VISIT_CONTEXT_FACTORY)) {
                        getDispenser().feedVisitContextFactory(className);
                    } else {
                        throw new IllegalStateException("Unexpected factory name " + factoryName);
                    }
                }
            }
        } catch (Throwable e) {
            throw new FacesException(e);
        }
    }
