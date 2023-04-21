    protected void startUp() {
        if (upnp != null) {
            refreshMappings();
            return;
        }
        final LoggerChannel core_log = plugin_interface.getLogger().getChannel("UPnP Core");
        try {
            upnp = UPnPFactory.getSingleton(new UPnPAdapter() {

                Set exception_traces = new HashSet();

                public SimpleXMLParserDocument parseXML(String data) throws SimpleXMLParserDocumentException {
                    return (plugin_interface.getUtilities().getSimpleXMLParserDocumentFactory().create(data));
                }

                public ResourceDownloaderFactory getResourceDownloaderFactory() {
                    return (plugin_interface.getUtilities().getResourceDownloaderFactory());
                }

                public UTTimer createTimer(String name) {
                    return (plugin_interface.getUtilities().createTimer(name, true));
                }

                public void createThread(String name, Runnable runnable) {
                    plugin_interface.getUtilities().createThread(name, runnable);
                }

                public Comparator getAlphanumericComparator() {
                    return (plugin_interface.getUtilities().getFormatters().getAlphanumericComparator(true));
                }

                public void trace(String str) {
                    core_log.log(str);
                    if (trace_to_log.getValue()) {
                        upnp_log_listener.log(str);
                    }
                }

                public void log(Throwable e) {
                    String nested = Debug.getNestedExceptionMessage(e);
                    if (!exception_traces.contains(nested)) {
                        exception_traces.add(nested);
                        if (exception_traces.size() > 128) {
                            exception_traces.clear();
                        }
                        core_log.log(e);
                    } else {
                        core_log.log(nested);
                    }
                }

                public void log(String str) {
                    log.log(str);
                }

                public String getTraceDir() {
                    return (plugin_interface.getUtilities().getAzureusUserDir());
                }
            }, getSelectedInterfaces());
            upnp.addRootDeviceListener(this);
            upnp_log_listener = new UPnPLogListener() {

                public void log(String str) {
                    log.log(str);
                }

                public void logAlert(String str, boolean error, int type) {
                    boolean logged = false;
                    if (alert_device_probs_param.getValue()) {
                        if (type == UPnPLogListener.TYPE_ALWAYS) {
                            log.logAlertRepeatable(error ? LoggerChannel.LT_ERROR : LoggerChannel.LT_WARNING, str);
                            logged = true;
                        } else {
                            boolean do_it = false;
                            if (type == UPnPLogListener.TYPE_ONCE_EVER) {
                                byte[] fp = plugin_interface.getUtilities().getSecurityManager().calculateSHA1(str.getBytes());
                                String key = "upnp.alert.fp." + plugin_interface.getUtilities().getFormatters().encodeBytesToString(fp);
                                PluginConfig pc = plugin_interface.getPluginconfig();
                                if (!pc.getPluginBooleanParameter(key, false)) {
                                    pc.setPluginParameter(key, true);
                                    do_it = true;
                                }
                            } else {
                                do_it = true;
                            }
                            if (do_it) {
                                log.logAlert(error ? LoggerChannel.LT_ERROR : LoggerChannel.LT_WARNING, str);
                                logged = true;
                            }
                        }
                    }
                    if (!logged) {
                        log.log(str);
                    }
                }
            };
            upnp.addLogListener(upnp_log_listener);
            mapping_manager.addListener(new UPnPMappingManagerListener() {

                public void mappingAdded(UPnPMapping mapping) {
                    addMapping(mapping);
                }
            });
            UPnPMapping[] upnp_mappings = mapping_manager.getMappings();
            for (int i = 0; i < upnp_mappings.length; i++) {
                addMapping(upnp_mappings[i]);
            }
            setNATPMPEnableState();
        } catch (Throwable e) {
            log.log(e);
        }
    }
