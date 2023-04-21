    public static void buildReportModule(String configFile, Rule rule) {
        InputStream in = null;
        XMLEventReader r = null;
        ReportEntry currentEntry = null;
        Report report = null;
        StringBuilder globalConditions = new StringBuilder();
        StringBuilder globalValuefilter = new StringBuilder();
        List<String> globalMapClass = new ArrayList<String>();
        String domain = null;
        String localdir = new StringBuilder().append(System.getProperty("user.dir")).append(File.separatorChar).toString();
        if (configFile == null || "".equals(configFile)) {
            String error = "configFile can not be null !";
            logger.error(error);
            throw new java.lang.RuntimeException(error);
        }
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (configFile.startsWith("file:")) {
                try {
                    in = new java.io.FileInputStream(new File(configFile.substring(configFile.indexOf("file:") + "file:".length())));
                } catch (Exception e) {
                    logger.error(e);
                }
                if (in == null) in = new java.io.FileInputStream(new File(localdir + configFile.substring(configFile.indexOf("file:") + "file:".length())));
            } else {
                URL url = loader.getResource(configFile);
                if (url == null) {
                    String error = "configFile: " + configFile + " not exist !";
                    logger.error(error);
                    throw new java.lang.RuntimeException(error);
                }
                in = url.openStream();
            }
            r = factory.createXMLEventReader(in);
            List<String> parents = new ArrayList<String>();
            while (r.hasNext()) {
                XMLEvent event = r.nextEvent();
                if (event.isStartElement()) {
                    StartElement start = event.asStartElement();
                    String tag = start.getName().getLocalPart();
                    if (tag.equalsIgnoreCase("domain")) {
                        if (start.getAttributeByName(new QName("", "value")) != null) {
                            domain = start.getAttributeByName(new QName("", "value")).getValue();
                            rule.setDomain(domain);
                        }
                        continue;
                    }
                    if (tag.equalsIgnoreCase("alias")) {
                        Alias alias = new Alias();
                        alias.setName(start.getAttributeByName(new QName("", "name")).getValue());
                        alias.setKey(start.getAttributeByName(new QName("", "key")).getValue());
                        rule.getAliasPool().put(alias.getName(), alias);
                        continue;
                    }
                    if (tag.equalsIgnoreCase("global-condition")) {
                        if (start.getAttributeByName(new QName("", "value")) != null) {
                            globalConditions.append(start.getAttributeByName(new QName("", "value")).getValue()).append("&");
                        }
                        continue;
                    }
                    if (tag.equalsIgnoreCase("global-mapClass")) {
                        if (start.getAttributeByName(new QName("", "value")) != null) {
                            globalMapClass.add(start.getAttributeByName(new QName("", "value")).getValue());
                        }
                        continue;
                    }
                    if (tag.equalsIgnoreCase("global-valuefilter")) {
                        if (start.getAttributeByName(new QName("", "value")) != null) {
                            globalValuefilter.append(start.getAttributeByName(new QName("", "value")).getValue()).append("&");
                        }
                        continue;
                    }
                    if (tag.equalsIgnoreCase("ReportEntry") || tag.equalsIgnoreCase("entry")) {
                        ReportEntry entry = new ReportEntry();
                        currentEntry = entry;
                        if (tag.equalsIgnoreCase("ReportEntry")) setReportEntry(true, start, entry, report, rule.getEntryPool(), rule.getAliasPool(), globalConditions, globalValuefilter, globalMapClass, parents); else {
                            setReportEntry(false, start, entry, report, rule.getEntryPool(), rule.getAliasPool(), globalConditions, globalValuefilter, globalMapClass, parents);
                        }
                        if (entry.getId() != null) {
                            rule.getEntryPool().put(entry.getId(), entry);
                        }
                        if (tag.equalsIgnoreCase("entry")) {
                            if (entry.getId() != null) rule.getReferEntrys().put(entry.getId(), entry); else if (report.getReportEntrys() != null && report.getReportEntrys().size() > 0) rule.getReferEntrys().put(report.getReportEntrys().get(report.getReportEntrys().size() - 1).getId(), report.getReportEntrys().get(report.getReportEntrys().size() - 1));
                        }
                        ReportEntry _tmpEntry = entry;
                        if (_tmpEntry.getId() == null && report.getReportEntrys() != null && report.getReportEntrys().size() > 0) _tmpEntry = report.getReportEntrys().get(report.getReportEntrys().size() - 1);
                        if (_tmpEntry.getBindingStack() != null) {
                            if (_tmpEntry.getValueExpression() != null && _tmpEntry.getValueExpression().indexOf("entry(") >= 0) for (String k : _tmpEntry.getBindingStack()) {
                                rule.getReferEntrys().put(k, null);
                            }
                        }
                        continue;
                    }
                    if (tag.equalsIgnoreCase("report")) {
                        if (report != null) rule.getReportPool().put(report.getId(), report);
                        report = new Report();
                        setReport(start, report, rule.getReportPool());
                        continue;
                    }
                    if (tag.equalsIgnoreCase("entryList")) {
                        report.setReportEntrys(new ArrayList<ReportEntry>());
                        continue;
                    }
                    if (tag.equalsIgnoreCase("alert")) {
                        ReportAlert alert = new ReportAlert();
                        setAlert(start, alert);
                        rule.getAlerts().add(alert);
                        continue;
                    }
                }
                if (event.isEndElement()) {
                    EndElement end = event.asEndElement();
                    String tag = end.getName().getLocalPart();
                    if (tag.equalsIgnoreCase("reports") && report != null) {
                        rule.getReportPool().put(report.getId(), report);
                        continue;
                    }
                }
            }
            for (Iterator<String> iterator = parents.iterator(); iterator.hasNext(); ) {
                String parent = iterator.next();
                ReportEntry parentEntry = rule.getEntryPool().get(parent);
                rule.getParentEntryPool().put(parent, parentEntry);
            }
            if (rule.getReferEntrys() != null && rule.getReferEntrys().size() > 0) {
                Iterator<Entry<String, ReportEntry>> iter = rule.getEntryPool().entrySet().iterator();
                StringBuilder invalidKeys = new StringBuilder();
                while (iter.hasNext()) {
                    Entry<String, ReportEntry> e = iter.next();
                    if (!rule.getReferEntrys().containsKey(e.getKey())) {
                        iter.remove();
                        invalidKeys.append(e.getKey()).append(",");
                    }
                }
                logger.error("File: " + configFile + " ----- remove invalid entry define : " + invalidKeys.toString());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (currentEntry != null && currentEntry.getName() != null) logger.error(new StringBuilder("Entry : ").append(currentEntry.getName()).toString());
            throw new RuntimeException("buildReportModule error!");
        } finally {
            try {
                if (r != null) r.close();
                if (in != null) in.close();
                r = null;
                in = null;
            } catch (Exception ex) {
                throw new RuntimeException("processConfigURL error !", ex);
            }
        }
    }
