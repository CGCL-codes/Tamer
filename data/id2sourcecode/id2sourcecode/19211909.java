    @PostConstruct
    public void init() {
        logger.info("+++++ TRACAuditChecklistMB: init() start ++++++++");
        tracSectionA = new ArrayList<TRACCriteriaCheckListItem>();
        tracSectionB = new ArrayList<TRACCriteriaCheckListItem>();
        tracSectionC = new ArrayList<TRACCriteriaCheckListItem>();
        Set<String> tableSet = tracAuditChecklistDataFacade.findAllTables();
        List<TracAuditChecklistData> tracData = tracAuditChecklistDataFacade.findAll();
        tracDataMap = new LinkedHashMap<String, String>();
        if (tableSet.contains(tracDataTable) && !tracData.isEmpty()) {
            logger.info("tracDataTable exists and not empty (read-back case): size=" + tracData.size());
            tracDataTableAvailable = true;
            String value = null;
            for (TracAuditChecklistData tracDataRow : tracData) {
                if (tracDataRow.getEvidence() == null) {
                    value = "";
                } else {
                    value = tracDataRow.getEvidence();
                }
                tracDataMap.put(tracDataRow.getAspectId(), value);
            }
            logger.info("size of tracDataMap=" + tracDataMap.size());
        } else {
            tracDataTableAvailable = false;
            logger.info("tracDataTable is not available: no-read-back case");
        }
        timestampPattern = configFile.getTimestampPattern();
        if (StringUtils.isBlank(timestampPattern)) {
            timestampPattern = TRACConstants.TIME_STAMP_PATTERN_FOR_WEB_PAGE;
        } else {
            if (!timestampPattern.endsWith(" zz") && !timestampPattern.endsWith(" Z")) {
                timestampPattern += " zz";
            }
        }
        logger.log(Level.INFO, "timestampPattern to be used={0}", timestampPattern);
        if (StringUtils.isBlank(configFile.getSaastimezone())) {
            timezoneId = TRACConstants.DEFAULT_TIME_ZONE;
        } else {
            timezoneId = configFile.getSaastimezone();
        }
        logger.log(Level.INFO, "timezone to be used={0}", timezoneId);
        fdf = FastDateFormat.getInstance(timestampPattern, TimeZone.getTimeZone(timezoneId));
        try {
            URL url = TRACAuditChecklistManagedBean.class.getResource(tracCriteriaPropertiesFileName);
            tracCriteriaCheckList = new LinkedProperties();
            tracCriteriaCheckList.load(url.openStream());
            Set<String> tmpKeys = tracCriteriaCheckList.stringPropertyNames();
            List<String> sortWrkList = new ArrayList<String>();
            sortWrkList.addAll(tmpKeys);
            sortList(sortWrkList);
            for (String key : sortWrkList) {
                String aspectCode = key.substring(aspectCodeOffsetValue);
                logger.info("aspectCode=" + aspectCode);
                if (aspectCode.startsWith("A")) {
                    if (aspectCode.equals("A")) {
                        sectionAcaption = tracCriteriaCheckList.getProperty(key);
                    } else {
                        if (aspectCode.length() == 4 || aspectCode.length() == 5) {
                            if (tracDataTableAvailable) {
                                tracSectionA.add(new TRACCriteriaCheckListItem(aspectCode, tracCriteriaCheckList.getProperty(key), tracDataMap.get(aspectCode), "", ""));
                            } else {
                                tracSectionA.add(new TRACCriteriaCheckListItem(aspectCode, tracCriteriaCheckList.getProperty(key), "", "", ""));
                            }
                            secAitems++;
                        }
                    }
                } else if (aspectCode.startsWith("B")) {
                    if (aspectCode.equals("B")) {
                        sectionBcaption = tracCriteriaCheckList.getProperty(key);
                    } else {
                        if (aspectCode.length() == 4 || aspectCode.length() == 5) {
                            if (tracDataTableAvailable) {
                                tracSectionB.add(new TRACCriteriaCheckListItem(aspectCode, tracCriteriaCheckList.getProperty(key), tracDataMap.get(aspectCode), "", ""));
                            } else {
                                tracSectionB.add(new TRACCriteriaCheckListItem(aspectCode, tracCriteriaCheckList.getProperty(key), "", "", ""));
                            }
                            secBitems++;
                        }
                    }
                } else if (aspectCode.startsWith("C")) {
                    if (aspectCode.equals("C")) {
                        sectionCcaption = tracCriteriaCheckList.getProperty(key);
                    } else {
                        if (aspectCode.length() == 4 || aspectCode.length() == 5) {
                            if (tracDataTableAvailable) {
                                tracSectionC.add(new TRACCriteriaCheckListItem(aspectCode, tracCriteriaCheckList.getProperty(key), tracDataMap.get(aspectCode), "", ""));
                            } else {
                                tracSectionC.add(new TRACCriteriaCheckListItem(aspectCode, tracCriteriaCheckList.getProperty(key), "", "", ""));
                            }
                            secCitems++;
                        }
                    }
                } else {
                    logger.warning("offset value is wrong: 16th character must be A or B or C");
                }
            }
        } catch (FileNotFoundException ex) {
            logger.log(Level.WARNING, "specified properties file was not found", ex);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "IO error occurred", ex);
        } finally {
        }
        aspectCount = secAitems + secBitems + secCitems;
        birtViewerServerUrl = configFile.getBirtViewerServerFrameset();
        if (tableSet.contains(tracResultTable)) {
            logger.info("tracResultTable exists");
            tracResultTableAvailable = true;
        } else {
            tracResultTableAvailable = false;
            logger.info("tracResultTable does not exist");
        }
        logger.info("+++++ TRACAuditChecklistMB: init() end   ++++++++");
    }
