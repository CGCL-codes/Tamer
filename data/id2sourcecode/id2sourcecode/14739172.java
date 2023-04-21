    public NagiosParserRet parse(NagiosLineInfo aNagiosInfo) {
        NagiosLineInfo info = null;
        try {
            logger.debug("Parsing new line ...");
            info = lineParser.parseLine(aNagiosInfo);
            if (info != null && info.getStatus() != null && info.getStatus().equals(NagiosStatus.STATUS_UNKNOWN)) {
                alertLog.writeAlertWarn(new NagiosException(ErrorCodes.CODE_1009), info);
            }
            log("Search plugin list for host name: " + info.getHostName() + " and service name: " + info.getServiceName() + "...", info);
            NagiosPluginDef[] pluginList = pluginCore.getPluginList(info.getHostName(), info.getServiceName());
            if ((pluginList == null) || (pluginList.length == 0)) {
                log("No plugin found.", info);
                discardedThread.writeLine(aNagiosInfo.getOriginalInfo());
                alertLog.writeAlertWarn(new NagiosException(ErrorCodes.CODE_1000), info);
                return null;
            } else {
                if (info.isTestLine()) {
                    testLogger.info("line parsed: " + aNagiosInfo.getOriginalInfo());
                    testLogger.info("Host: " + info.getHostName());
                    testLogger.info("Service: " + info.getServiceName());
                    testLogger.info("Last Check: " + info.getLastCheck());
                    testLogger.info("Status: " + info.getStatus());
                    testLogger.info("check duration: " + info.getCheckDuration());
                }
                HashMap<String, PerformanceDataDef> perfDataFound = new HashMap<String, PerformanceDataDef>();
                for (int i = 0; i < pluginList.length; i++) {
                    NagiosPluginDef myPlugin = pluginList[i];
                    boolean isTest = info.isTestLine() || myPlugin.isTest();
                    boolean storeResult = info.isTestLine() || !myPlugin.isTest();
                    NagiosParserable parser = myPlugin.getInstance();
                    List<PerformanceDataDef> perfDataList = null;
                    if (myPlugin.isParseOutput() && myPlugin.matchOutput(info.getOutput())) {
                        try {
                            log("Parsing output ...", info, myPlugin);
                            perfDataList = parser.parseOutput(info.getOutput());
                            if (perfDataList != null) {
                                if (storeResult) {
                                    analyzePerformanceData(perfDataList, perfDataFound, myPlugin, info);
                                }
                                printPerformancesData(isTest, new ArrayList<PerformanceDataDef>(perfDataList));
                            }
                        } catch (NagiosException ex) {
                            logWarn("Error parsing output: " + ex.toString(), info);
                            alertLog.writeAlertWarn(new NagiosException(ErrorCodes.CODE_1001, ex.toString()), info);
                        }
                    }
                    if (myPlugin.isParsePerfData()) {
                        try {
                            if (perfDataList != null) {
                                perfDataList.clear();
                            }
                            log("Parsing performance data output ...", info);
                            perfDataList = parser.parsePerformanceData(info.getPerfDataOutput());
                            if (perfDataList != null) {
                                if (storeResult) {
                                    analyzePerformanceData(perfDataList, perfDataFound, myPlugin, info);
                                }
                                printPerformancesData(isTest, new ArrayList<PerformanceDataDef>(perfDataList));
                            }
                        } catch (NagiosException ex) {
                            logWarn("Error parsing perfData: " + ex.toString(), info);
                            alertLog.writeAlertWarn(new NagiosException(ErrorCodes.CODE_1002, ex.toString()), info);
                        }
                    }
                }
                Host ret = new Host();
                ret.setName(info.getHostName());
                Service service = new Service();
                service.setName(info.getServiceName());
                ret.addService(service);
                ServiceStatus status = new ServiceStatus();
                status.setCheckDate(new Timestamp(info.getLastCheck().getTime()));
                status.setStatus(info.getStatus().getState());
                status.setVerificationTime(info.getCheckDuration());
                for (PerformanceDataDef perfData : perfDataFound.values()) {
                    logger.debug("found performance data: " + perfData);
                    ServiceData srvData = new ServiceData();
                    srvData.setName(perfData.getName());
                    srvData.setUom(perfData.getUom());
                    PerformanceData pdata = new PerformanceData();
                    pdata.setValue(perfData.getValue());
                    pdata.setMinValue(perfData.getMinValue());
                    pdata.setMaxValue(perfData.getMaxValue());
                    pdata.setWarning(perfData.getWarning());
                    pdata.setCritical(perfData.getCritical());
                    srvData.addPerformanceData(pdata);
                    service.addServiceData(srvData);
                }
                if (perfDataFound.size() == 0) {
                    noPerfDataFoundThread.writeLine(aNagiosInfo.getOriginalInfo());
                    alertLog.writeAlertWarn(new NagiosException(ErrorCodes.CODE_1003), info);
                }
                service.addStatus(status);
                return new NagiosParserRet(ret, info.isTestLine());
            }
        } catch (BaseException ex) {
            logError("Error parsing line: " + ex.toString(), info);
            alertLog.writeAlertError(new NagiosException(ErrorCodes.CODE_1004), info);
            try {
                discardedThread.writeLine(aNagiosInfo.getOriginalInfo());
            } catch (Exception iox) {
                logError("Error writing line to discarded file: ", iox, info);
            }
            return null;
        } catch (Throwable ex) {
            logError("Error parsing line:", ex, info);
            alertLog.writeAlertError(new NagiosException(ErrorCodes.CODE_1004), info);
            try {
                discardedThread.writeLine(aNagiosInfo.getOriginalInfo());
            } catch (Exception iox) {
                logError("Error writing line to discarded file: ", iox, info);
            }
            return null;
        }
    }
