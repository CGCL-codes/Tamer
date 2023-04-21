    private static void loadConfigFromFile() {
        String pairSeparator = "|";
        try {
            ClassLoader classLoader = getContextClassLoader(null);
            URL url = null;
            url = classLoader.getResource(configurationFileLoc);
            if (url == null) {
                File file = new File(configurationFileLoc);
                url = file.toURL();
            }
            if (url != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Found URL to MetadataFactoryRegistry configuration file: " + configurationFileLoc);
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String line = reader.readLine();
                if (line != null && line.indexOf("|") != -1) {
                    String interfaceName = line.substring(0, line.indexOf(pairSeparator));
                    String implName = line.substring(line.indexOf(pairSeparator) + 1, line.length());
                    if (log.isDebugEnabled()) {
                        log.debug("For registered class: " + interfaceName + " the " + "following implementation was found: " + implName);
                    }
                    Class intf = classLoader.loadClass(interfaceName);
                    Class impl = classLoader.loadClass(implName);
                    if (intf != null && impl != null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Loaded both interface and implementation class: " + interfaceName + ":" + implName);
                        }
                        if (impl.getEnclosingClass() == null) {
                            table.put(intf, impl.newInstance());
                        } else {
                            if (log.isWarnEnabled()) {
                                log.warn("The implementation class: " + impl.getClass().getName() + " could not be lregistered because it is an inner class. " + "In order to register file-based overrides, implementations " + "must be public outer classes.");
                            }
                        }
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("Could not load both interface and implementation class: " + interfaceName + ":" + implName);
                        }
                    }
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("Did not find File for MetadataFactoryRegistry configuration " + "file: " + configurationFileLoc);
                    }
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Did not find URL for MetadataFactoryRegistry configuration " + "file: " + configurationFileLoc);
                }
            }
        } catch (Throwable t) {
            if (log.isDebugEnabled()) {
                log.debug("The MetadataFactoryRegistry could not process the configuration file: " + configurationFileLoc + " because of the following error: " + t.toString());
            }
        }
    }
