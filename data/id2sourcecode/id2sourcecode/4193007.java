    private static boolean registerFromFile(URL providers) {
        boolean registeredSomething = false;
        InputStream urlStream = null;
        try {
            urlStream = providers.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlStream, "UTF-8"));
            String provider;
            while ((provider = reader.readLine()) != null) {
                int comment = provider.indexOf('#');
                if (comment != -1) {
                    provider = provider.substring(0, comment);
                }
                provider = provider.trim();
                if (0 == provider.length()) {
                    continue;
                }
                try {
                    String[] parts = provider.split("\\s", 3);
                    if (3 == parts.length) {
                        ModuleSpecID msid = ModuleSpecID.create(URI.create(parts[0]));
                        String code = parts[1];
                        String description = parts[2];
                        ModuleImplAdvertisement moduleImplAdv;
                        try {
                            Class<Module> moduleClass = (Class<Module>) Class.forName(code);
                            Method getImplAdvMethod = moduleClass.getMethod("getDefaultModuleImplAdvertisement");
                            moduleImplAdv = (ModuleImplAdvertisement) getImplAdvMethod.invoke(null);
                        } catch (Exception failed) {
                            moduleImplAdv = StdPeerGroup.mkImplAdvBuiltin(msid, code, description);
                        }
                        getJxtaLoader().defineClass(moduleImplAdv);
                        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                            LOG.fine("Registered Module " + msid + " : " + parts[1]);
                        }
                    } else {
                        if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                            LOG.log(Level.WARNING, "Failed to register \'" + provider + "\'");
                        }
                    }
                } catch (Exception allElse) {
                    if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                        LOG.log(Level.WARNING, "Failed to register \'" + provider + "\'", allElse);
                    }
                }
            }
        } catch (IOException ex) {
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                LOG.log(Level.WARNING, "Failed to read provider list " + providers, ex);
            }
        } finally {
            if (null != urlStream) {
                try {
                    urlStream.close();
                } catch (IOException ignored) {
                }
            }
        }
        return registeredSomething;
    }
