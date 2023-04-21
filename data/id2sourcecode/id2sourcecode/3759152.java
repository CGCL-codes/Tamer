    @Override
    public LOCKSSDaemonStatusTableTO getDataFromDaemonStatusTableByHttps() throws HttpResponseException {
        LOCKSSDaemonStatusTableXmlStreamParser ldstxp = null;
        LOCKSSDaemonStatusTableTO ldstTO = null;
        HttpEntity entity = null;
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("HttpClientV3PollerDetailTableDAO", HttpClientV3PollerDetailTableDAO.class);
        MySQLSuccessfulPollsDAO mspDao = (MySQLSuccessfulPollsDAO) ((MySQLDAOFactory) DAOFactory.getDAOFactory(DAOFactory.DBvendor.MySQL)).getSuccessfulPollsDAO();
        if (mspDao == null) {
            logger.log(Level.WARNING, "MySQLSuccessfulPollsDAO mspDao is still null");
            ldstTO = new LOCKSSDaemonStatusTableTO();
            return ldstTO;
        }
        MySQLSuccessfulReplicaIpDAO msriDao = (MySQLSuccessfulReplicaIpDAO) ((MySQLDAOFactory) DAOFactory.getDAOFactory(DAOFactory.DBvendor.MySQL)).getSuccessfulReplicaIpDAO();
        if (msriDao == null) {
            logger.log(Level.WARNING, "MySQLSuccessfulReplicaIpDAO msriDao is still null");
            ldstTO = new LOCKSSDaemonStatusTableTO();
            return ldstTO;
        }
        Set<String> storedPollIds = mspDao.getStoredPollIds();
        Set<String> pollIdsFromMsriDao = msriDao.getStoredPollIds();
        Set<String> storedBoth = getIntersection(storedPollIds, pollIdsFromMsriDao);
        Set<String> voterNotSavedPollIdSet = mspDao.getVoterNotSavedPollIds();
        logger.log(Level.INFO, "how many 2nd  stored poll ids={0}", pollIdsFromMsriDao.size());
        logger.log(Level.INFO, "how many both stored poll ids={0}", storedBoth.size());
        logger.log(Level.INFO, "how many successful polls whose voters are not saved={0}", voterNotSavedPollIdSet.size());
        Map<String, Long> pollIdToDuration = new LinkedHashMap<String, Long>();
        Map<String, String> pollIdToAuName = new LinkedHashMap<String, String>();
        Map<String, Long> pollIdToEndTime = new LinkedHashMap<String, Long>();
        Map<String, List<String>> pollIpaddressMap = new LinkedHashMap<String, List<String>>();
        if (targetPollIdSet != null && !targetPollIdSet.isEmpty()) {
            for (String targetPollID : targetPollIdSet) {
                try {
                    logger.log(Level.INFO, "working on targetPollId={0}", targetPollID);
                    this.targetPollId = targetPollID;
                    this.dataUrl = updateDataUrl();
                    if (!storedBoth.contains(targetPollID)) {
                        logger.log(Level.FINE, "collect detailed poll data for targetPollId={0}", targetPollID);
                        HttpGet httpget = null;
                        try {
                            httpget = new HttpGet(dataUrl);
                            logger.log(Level.INFO, "executing request={0}", httpget.getURI());
                            HttpResponse resp = httpClient.execute(httpget);
                            int statusCode = resp.getStatusLine().getStatusCode();
                            if (statusCode != HttpStatus.SC_OK) {
                                logger.log(Level.WARNING, "response to the request is not OK: skip this IP: status code={0}", statusCode);
                                httpget.abort();
                                ldstTO = new LOCKSSDaemonStatusTableTO();
                                ldstTO.setBoxHttpStatusOK(false);
                                ldstTO.setCurrentPollId(targetPollID);
                                return ldstTO;
                            }
                            entity = resp.getEntity();
                            InputStream is = entity.getContent();
                            ldstxp = new LOCKSSDaemonStatusTableXmlStreamParser();
                            ldstxp.read(new BufferedInputStream(is));
                            ldstTO = ldstxp.getLOCKSSDaemonStatusTableTO();
                            if (ldstTO.getCurrentPollId() == null) {
                                logger.log(Level.INFO, "currentPollId is null: assign targetPollId:{0}", targetPollID);
                                ldstTO.setCurrentPollId(targetPollID);
                            }
                            logger.log(Level.INFO, "After parsing {0} : poll id={1}: contents of {2}", new Object[] { this.tableId, targetPollID, ldstTO });
                            if (ldstTO.hasIncompleteRows) {
                                logger.log(Level.WARNING, "!!!!!!!!! incomplete rows are found for {0} : poll_id={1} !!!!!!!!!", new Object[] { tableId, targetPollID });
                                if (ldstTO.getTableData() != null && ldstTO.getTableData().size() > 0) {
                                    logger.log(Level.FINE, "table(map) data:\n{0}", xstream.toXML(ldstTO.getTableData()));
                                }
                            } else {
                                logger.log(Level.FINE, "All rows are complete for {0} : poll id={1}", new Object[] { tableId, targetPollID });
                                if (ldstTO.getTableData() != null && ldstTO.getTableData().size() > 0) {
                                    logger.log(Level.FINE, "table(map) data:\n{0}", xstream.toXML(ldstTO.getTableData()));
                                }
                                if (ldstTO.getTabularData() != null && ldstTO.getTabularData().size() > 0) {
                                    logger.log(Level.FINE, "tablar(list) data:\n{0}", xstream.toXML(ldstTO.getTabularData()));
                                }
                            }
                            logger.log(Level.FINE, "ldstTO: SummaryInfo (poll id={0}):\n{1}", new Object[] { targetPollID, xstream.toXML(ldstTO.getSummaryInfoList()) });
                            if (!voterNotSavedPollIdSet.contains(targetPollID)) {
                                String actualEnd = null;
                                String startTime = null;
                                long estimatedDuration = 0L;
                                long endTime = 0L;
                                String auName = null;
                                for (SummaryInfo si : ldstTO.getSummaryInfoList()) {
                                    if (si.getTitle().equals("Actual End")) {
                                        logger.log(Level.INFO, "Actual End={0}", si.toString());
                                        if (!storedPollIds.contains(targetPollID)) {
                                            logger.log(Level.FINE, "Adding actual-end data({1}) for this pollId={0}):", new Object[] { targetPollID, si.getValue() });
                                            actualEnd = si.getValue();
                                            logger.log(Level.INFO, "acutaulEnd={0}", actualEnd);
                                        } else {
                                            logger.log(Level.WARNING, "Not adding actual-end data (already on the table) for this id={0}", targetPollID);
                                        }
                                    } else if (si.getTitle().equals("Start Time")) {
                                        logger.log(Level.INFO, "Start Time={0}", si.toString());
                                        if (!storedPollIds.contains(targetPollID)) {
                                            logger.log(Level.FINE, "Adding start-time data({1}) for this pollId):{0}", new Object[] { targetPollID, si.getValue() });
                                            startTime = si.getValue();
                                            logger.log(Level.INFO, "startTime={0}", startTime);
                                        } else {
                                            logger.log(Level.WARNING, "Not adding start-time data (already on the table):", targetPollID);
                                        }
                                    }
                                    if (si.getTitle().equals("Duration")) {
                                        if (!storedPollIds.contains(targetPollID)) {
                                            logger.log(Level.FINE, "estimated duration data for this pollId={0}):{1}", new Object[] { targetPollID, si.getValue() });
                                            estimatedDuration = DaemonStatusDataUtil.durationStringToMilliSeconds(si.getValue());
                                        }
                                    }
                                    if (si.getTitle().equals("Volume")) {
                                        if (!storedPollIds.contains(targetPollID)) {
                                            logger.log(Level.FINE, "auName for this pollId={0}) = {1}", new Object[] { targetPollID, si.getValue() });
                                            if (StringUtils.isNotBlank(si.getValue())) {
                                                auName = si.getValue();
                                            } else {
                                                auName = AU_NAME_PLACEHOLDER;
                                            }
                                        }
                                    }
                                }
                                pollIdToAuName.put(targetPollID, auName);
                                logger.log(Level.INFO, "actualEnd={0}; startTime={1}", new Object[] { actualEnd, startTime });
                                if (actualEnd != null && startTime != null) {
                                    long actualDuration = DaemonStatusDataUtil.getEpocTimeFromString(actualEnd, this.timezoneOffset) - DaemonStatusDataUtil.getEpocTimeFromString(startTime, this.timezoneOffset);
                                    endTime = DaemonStatusDataUtil.getEpocTimeFromString(actualEnd, this.timezoneOffset);
                                    logger.log(Level.INFO, "actual duration is available={0}", actualDuration);
                                    if (!storedPollIds.contains(targetPollID)) {
                                        logger.log(Level.FINE, "Adding actual duration data for this pollId):{0}", targetPollID);
                                        pollIdToDuration.put(targetPollID, actualDuration);
                                        pollIdToEndTime.put(targetPollID, endTime);
                                    } else {
                                        logger.log(Level.WARNING, "Not adding actual duration data(already on the table):{0}", targetPollID);
                                    }
                                } else {
                                    logger.log(Level.INFO, "actual duration cannot be calculated for this pollId ({0}): use the estimate ={1}", new Object[] { targetPollID, estimatedDuration });
                                    if (!storedPollIds.contains(targetPollID)) {
                                        logger.log(Level.FINE, "Adding estimated duration data for this pollId):{0}", targetPollID);
                                        pollIdToDuration.put(targetPollID, estimatedDuration);
                                        endTime = DaemonStatusDataUtil.getEpocTimeFromString(startTime, this.timezoneOffset) + estimatedDuration;
                                        pollIdToEndTime.put(targetPollID, endTime);
                                    } else {
                                        logger.log(Level.WARNING, "Not adding estimated duration data(already on the table):{0}", targetPollID);
                                    }
                                }
                                logger.log(Level.INFO, "endTime={1} for this pollId({0})", new Object[] { targetPollID, endTime });
                            } else {
                                logger.log(Level.WARNING, "The poll data of this pollId={0} had been already saved in the past: skip this pollId", targetPollID);
                            }
                            if (ldstTO.getCurrentSuccessfulReplicaIpList() != null) {
                                if (!pollIdsFromMsriDao.contains(targetPollID)) {
                                    List<String> rawIpList = ldstTO.getCurrentSuccessfulReplicaIpList();
                                    logger.log(Level.FINE, "poll caller IP address={0} and this voter ip={1}", new Object[] { ldstTO.getIpAddress(), this.ip });
                                    if (this.ip != null) {
                                        rawIpList.add(this.ip);
                                        ldstTO.setIpAddress(this.ip);
                                        ldstTO.setCurrentSuccessfulReplicaIpList(rawIpList);
                                    }
                                    logger.log(Level.FINE, "ldstTO.getCurrentSuccessfulReplicaIpList()={0}", ldstTO.getCurrentSuccessfulReplicaIpList());
                                    pollIpaddressMap.put(targetPollID, rawIpList);
                                    logger.log(Level.INFO, "adding poll ip data for the poll:{0}:size={1}", new Object[] { targetPollID, ldstTO.getCurrentSuccessfulReplicaIpList().size() });
                                } else {
                                    logger.log(Level.INFO, "Not adding poll ip data for the poll(already on the table):{0}", targetPollID);
                                }
                            } else {
                                logger.log(Level.FINE, "Not adding poll-ip data for the pollId ({0}): no voter-ip exists", targetPollID);
                            }
                        } catch (ConnectTimeoutException ce) {
                            logger.log(Level.WARNING, "ConnectTimeoutException occurred", ce);
                            ldstTO = new LOCKSSDaemonStatusTableTO();
                            ldstTO.setBoxHttpStatusOK(false);
                            ldstTO.setCurrentPollId(targetPollID);
                            if (httpget != null) {
                                httpget.abort();
                            }
                            return ldstTO;
                        } catch (SocketTimeoutException se) {
                            logger.log(Level.WARNING, "SocketTimeoutException occurred", se);
                            ldstTO = new LOCKSSDaemonStatusTableTO();
                            ldstTO.setBoxHttpStatusOK(false);
                            ldstTO.setCurrentPollId(targetPollID);
                            if (httpget != null) {
                                httpget.abort();
                            }
                            return ldstTO;
                        } catch (ClientProtocolException pe) {
                            logger.log(Level.SEVERE, "The protocol was not http; https is suspected", pe);
                            ldstTO = new LOCKSSDaemonStatusTableTO();
                            ldstTO.setBoxHttpStatusOK(false);
                            ldstTO.setHttpProtocol(false);
                            if (httpget != null) {
                                httpget.abort();
                            }
                            return ldstTO;
                        } catch (IOException ex) {
                            logger.log(Level.SEVERE, "IO exception occurs", ex);
                            ldstTO = new LOCKSSDaemonStatusTableTO();
                            ldstTO.setBoxHttpStatusOK(false);
                            ldstTO.setCurrentPollId(targetPollID);
                            if (httpget != null) {
                                httpget.abort();
                            }
                            return ldstTO;
                        } finally {
                            if (entity != null) {
                                try {
                                    EntityUtils.consume(entity);
                                } catch (IOException ex) {
                                    logger.log(Level.SEVERE, "io exception when entity was to be consumed", ex);
                                }
                            }
                        }
                    } else {
                        logger.log(Level.WARNING, "Skip this ip (already on the table)={0}", targetPollID);
                        ldstTO = new LOCKSSDaemonStatusTableTO();
                    }
                    logger.log(Level.FINE, "ldstTO (poll id={0}):\n{1}", new Object[] { targetPollID, xstream.toXML(ldstTO) });
                } catch (UnsupportedEncodingException ex) {
                    logger.log(Level.SEVERE, "UnsupportedEncodingException", ex);
                }
            }
            ldstTO.setSuccessfulReplicaIpMap(pollIpaddressMap);
            ldstTO.setPollIdToDurationMap(pollIdToDuration);
            ldstTO.setTargetPollIdSet(targetPollIdSet);
            ldstTO.setPollIdToAuNameMap(pollIdToAuName);
            ldstTO.setPollIdToEndTimeMap(pollIdToEndTime);
        } else {
            logger.log(Level.WARNING, "target poll id set is empty or null");
            ldstTO = new LOCKSSDaemonStatusTableTO();
        }
        return ldstTO;
    }
