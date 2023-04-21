    @PostConstruct
    public void saasStartup() {
        Properties gfJvmProps = System.getProperties();
        if (gfJvmProps.containsKey("saas.localconfig.file")) {
            sassLocalConfigFileName = gfJvmProps.getProperty("saas.localconfig.file");
            if (StringUtils.isNotBlank(sassLocalConfigFileName)) {
                try {
                    logger.log(Level.INFO, "sassLocalConfigFileName={0}", sassLocalConfigFileName);
                    Properties localConfigProps = new Properties();
                    InputStream is = null;
                    try {
                        is = new FileInputStream(sassLocalConfigFileName);
                        localConfigProps.loadFromXML(is);
                        if (logger.isLoggable(Level.FINE)) {
                            for (String key : localConfigProps.stringPropertyNames()) {
                                logger.log(Level.FINE, "key={0}:value={1}", new Object[] { key, localConfigProps.getProperty(key) });
                            }
                        }
                        saasConfigProperties.putAll(localConfigProps);
                    } catch (FileNotFoundException ex) {
                        logger.log(Level.WARNING, "specified config file was not found", ex);
                    } catch (IOException ex) {
                        logger.log(Level.WARNING, "IO error occurred", ex);
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException ex) {
                                logger.log(Level.WARNING, "failed to close the opened local config file", ex);
                            }
                        }
                    }
                    URL url = SAASConfigurationRegistryBean.class.getResource(SAAS_BUILD_INFO_FILENAME);
                    saasBuildInfo.load(url.openStream());
                    Set<String> tmpKeys = saasBuildInfo.stringPropertyNames();
                    for (String ky : tmpKeys) {
                        logger.log(Level.FINE, "{0}(from the saasconfig)={1}", new Object[] { ky, saasConfigProperties.getProperty(ky) });
                        logger.log(Level.FINE, "{0}(from the properties)={1}", new Object[] { ky, saasBuildInfo.getProperty(ky) });
                        saasConfigProperties.put(ky, saasBuildInfo.getProperty(ky));
                    }
                    logger.log(Level.FINE, "sassConfigProperties={0}", saasConfigProperties.toString());
                    String targetPropertyKey = "saas.member.ip.tag";
                    String targetPropertyValue = saasConfigProperties.getProperty(targetPropertyKey);
                    if (StringUtils.isBlank(targetPropertyValue)) {
                        targetPropertyValue = PLN_MEMBER_IP_LIST_KEY;
                    } else {
                        targetPropertyValue = targetPropertyValue.trim();
                    }
                    logger.log(Level.INFO, "targetPropertyValue={0}", targetPropertyValue);
                    String lockssXmlUrl = (saasConfigProperties.getProperty("saas.lockss.xml.url")).trim();
                    logger.log(Level.INFO, "lockss.xml url={0}", lockssXmlUrl);
                    if (StringUtils.isBlank(lockssXmlUrl)) {
                        logger.log(Level.WARNING, "lockss.xml is not specified");
                        saasConfigProperties.put("saas.ip.fromlockssxml", "");
                    } else {
                        PLNConfigFileXPathReader xpathReader = new PLNConfigFileXPathReader();
                        Map<String, List<String>> resultMap = xpathReader.read(lockssXmlUrl, "UTF-8");
                        if (resultMap != null && resultMap.containsKey(targetPropertyValue)) {
                            logger.log(Level.INFO, "raw ip list={0}", resultMap.get(targetPropertyValue));
                            ipSet.addAll(PLNmemberIpDAO.getPeerIpAddresses(resultMap.get(targetPropertyValue)));
                            logger.log(Level.INFO, "ipSet: lockss.xml={0}", ipSet);
                        }
                        if (resultMap != null && resultMap.containsKey("quorum")) {
                            logger.log(Level.INFO, "quorum={0}", resultMap.get("quorum"));
                        }
                        if (resultMap != null && resultMap.containsKey("maxPollDuration")) {
                            logger.log(Level.INFO, "maxPollDuration={0}", resultMap.get("maxPollDuration"));
                        }
                        saasConfigProperties.put("saas.ip.fromlockssxml", StringUtils.join(ipSet, ","));
                    }
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "sassLocalConfigFile cannot be read", ex);
                }
            } else {
                logger.log(Level.INFO, "sassLocalConfigFileName is null or empty");
            }
        } else {
            logger.log(Level.INFO, "saas.localconfig.file is not included in the JVM options");
        }
        if (gfJvmProps.containsKey("saas.captcha.privatekey")) {
            saasConfigProperties.put("saas.captcha.privatekey", gfJvmProps.getProperty("saas.captcha.privatekey"));
        } else {
            logger.log(Level.INFO, "saas.captcha.privatekey is not included in the JVM options");
        }
        if (!saasConfigProperties.containsKey("saas.timezone") || StringUtils.isBlank(saasConfigProperties.getProperty("saas.timezone"))) {
            logger.log(Level.INFO, "saas.timezone is not stored in saas-local-config.xml: check JVM options");
            if (gfJvmProps.containsKey("saas.timezone") && StringUtils.isNotBlank(gfJvmProps.getProperty("saas.timezone"))) {
                logger.log(Level.INFO, "saas.timezone is stored in JVM options and it is not blank={0}", gfJvmProps.containsKey("saas.timezone"));
                saasConfigProperties.put("saas.timezone", gfJvmProps.getProperty("saas.timezone"));
            } else {
                logger.log(Level.INFO, "saas.timezone is not specified in the JVM options: use the default=[{0}]", SAAS_DEFAULT_TIME_ZONE);
            }
        }
        logger.log(Level.INFO, "saas.timezone is {0}", saasConfigProperties.getProperty("saas.timezone"));
        saasAuditConfigFileName = gfJvmProps.getProperty("saas.audit.config.file");
        if (StringUtils.isNotBlank(saasAuditConfigFileName)) {
            logger.log(Level.INFO, "saasAuditConfigFileName={0}", saasAuditConfigFileName);
            Properties auditConfigProps = new Properties();
            InputStream is = null;
            try {
                is = new FileInputStream(saasAuditConfigFileName);
                auditConfigProps.loadFromXML(is);
                for (String key : auditConfigProps.stringPropertyNames()) {
                    logger.log(Level.FINE, "key={0}:value={1}", new Object[] { key, auditConfigProps.getProperty(key) });
                    if (key.equals("saas.targetIp")) {
                        auditConfigProps.setProperty(key, auditConfigProps.getProperty(key).replaceAll("\\s", ""));
                    }
                    if (key.equals("saas.audit.config.timestamp.pattern")) {
                        auditConfigProps.setProperty(key, auditConfigProps.getProperty(key).replaceAll("[:\\?\\|\\$/\\*\\\\]", "-"));
                    }
                }
                saasAuditConfigProperties.putAll(auditConfigProps);
            } catch (FileNotFoundException ex) {
                logger.log(Level.WARNING, "specified audit config file was not found", ex);
            } catch (IOException ex) {
                logger.log(Level.WARNING, "saasAuditConfigFile cannot be read", ex);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        logger.log(Level.WARNING, "failed to close the audit config file", ex);
                    }
                }
            }
        } else {
            logger.log(Level.INFO, "no saved audit-config file");
        }
        timestampPattern = saasAuditConfigProperties.getProperty("saas.audit.config.timestamp.pattern");
        if (StringUtils.isBlank(timestampPattern)) {
            timestampPattern = SAAS_TIME_STAMP_PATTERN;
            logger.log(Level.INFO, "time-stamp pattern is set to the default[{0}]", timestampPattern);
        } else {
            logger.log(Level.INFO, "time-stamp pattern is set to [{0}]", timestampPattern);
        }
        Set<String> tmpKeys = saasBuildInfo.stringPropertyNames();
        String plnAuditParamPrefix = "saas.audit.pln.config.";
        for (String ky : tmpKeys) {
            if (plnBoundAuditParameterKeySet.contains(ky)) {
                logger.log(Level.FINE, "{0}(to be added)={1}", new Object[] { ky, saasBuildInfo.getProperty(ky) });
                saasAuditConfigProperties.put(plnAuditParamPrefix + ky, saasBuildInfo.getProperty(ky));
            }
        }
        xstream = new XStream(new JsonHierarchicalStreamDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("SAASConfigurationRegistryBean", SAASConfigurationRegistryBean.class);
    }
