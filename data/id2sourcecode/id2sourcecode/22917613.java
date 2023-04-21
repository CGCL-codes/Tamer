    public Command execute() throws RollBackCommandException {
        Session sess = null;
        DictionaryHelper dh;
        FlowCell flowCell = null;
        if (workItemXMLString != null || this.dirtyWorkItemXMLString != null) {
            try {
                sess = HibernateSession.currentSession(this.getUsername());
                dh = DictionaryHelper.getInstance(sess);
                parser.parse(sess);
                if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
                    if (this.workItemXMLString != null) {
                        flowCell = new FlowCell();
                        flowCell.setBarcode(flowCellBarcode);
                        sess.save(flowCell);
                        sess.flush();
                        flowCell.setNumber("FC" + flowCell.getIdFlowCell());
                        flowCell.setCodeSequencingPlatform(codeStepNext.equals(Step.SEQ_RUN) ? SequencingPlatform.ILLUMINA_GAIIX_SEQUENCING_PLATFORM : SequencingPlatform.ILLUMINA_HISEQ_2000_SEQUENCING_PLATFORM);
                        java.sql.Date flowCellDate = null;
                        if (flowCellDateStr != null) {
                            flowCellDate = this.parseDate(flowCellDateStr);
                        } else {
                            flowCellDate = new java.sql.Date(System.currentTimeMillis());
                        }
                        flowCell.setCreateDate(flowCellDate);
                        if (flowCellRunNumberStr != null && !flowCellRunNumberStr.equals("")) {
                            flowCell.setRunNumber(new Integer(flowCellRunNumberStr));
                        }
                        if (flowCellSide != null) {
                            flowCell.setSide(flowCellSide);
                        }
                        if (flowCellIdSeqRunTypeStr != null && !flowCellIdSeqRunTypeStr.equals("")) {
                            flowCell.setIdSeqRunType(new Integer(flowCellIdSeqRunTypeStr));
                        }
                        if (flowCellIdInstrumentStr != null && !flowCellIdInstrumentStr.equals("")) {
                            flowCell.setIdInstrument(new Integer(flowCellIdInstrumentStr));
                        }
                        Integer flowCellNumCycles = null;
                        if (flowCellNumCyclesStr != null && !flowCellNumCyclesStr.equals("")) {
                            flowCellNumCycles = new Integer(flowCellNumCyclesStr);
                        }
                        String runFolder = flowCell.getRunFolderName(dh);
                        TreeSet channels = new TreeSet(new FlowCellChannelComparator());
                        int laneNumber = 1;
                        HashMap requestNumbers = new HashMap();
                        HashMap idOrganisms = new HashMap();
                        int maxCycles = 0;
                        Integer idNumberSequencingCycles = null;
                        for (Iterator i = parser.getChannelNumbers().iterator(); i.hasNext(); ) {
                            String channelNumber = (String) i.next();
                            FlowCellChannel channel = new FlowCellChannel();
                            channel.setNumber(new Integer(laneNumber));
                            channel.setIdFlowCell(flowCell.getIdFlowCell());
                            sess.save(channel);
                            sess.flush();
                            if (runFolder != null) {
                                channel.setFileName(runFolder);
                            }
                            channel.setSampleConcentrationpM(parser.getSampleConcentrationpm(channelNumber));
                            channel.setIsControl(parser.getIsControl(channelNumber));
                            channel.setNumberSequencingCyclesActual(flowCellNumCycles);
                            List channelContents = parser.getChannelContents(channelNumber);
                            for (Iterator i1 = channelContents.iterator(); i1.hasNext(); ) {
                                WorkItemSolexaAssembleParser.ChannelContent content = (WorkItemSolexaAssembleParser.ChannelContent) i1.next();
                                if (content.getSequenceLane() != null) {
                                    SequenceLane lane = content.getSequenceLane();
                                    lane.setIdFlowCellChannel(channel.getIdFlowCellChannel());
                                    if (flowCell.getIdSeqRunType() == null) {
                                        flowCell.setIdSeqRunType(lane.getIdSeqRunType());
                                    }
                                    Integer seqCycles = new Integer(dh.getNumberSequencingCycles(lane.getIdNumberSequencingCycles()));
                                    if (idNumberSequencingCycles == null || seqCycles.intValue() > maxCycles) {
                                        idNumberSequencingCycles = lane.getIdNumberSequencingCycles();
                                        maxCycles = seqCycles.intValue();
                                    }
                                    WorkItem workItem = content.getWorkItem();
                                    requestNumbers.put(workItem.getRequest().getNumber(), null);
                                    idOrganisms.put(lane.getSample().getIdOrganism(), null);
                                    sess.delete(workItem);
                                } else if (content.getSequenceControl() != null) {
                                    channel.setIdSequencingControl(content.getSequenceControl().getIdSequencingControl());
                                }
                            }
                            WorkItem wi = new WorkItem();
                            wi.setFlowCellChannel(channel);
                            wi.setCodeStepNext(codeStepNext);
                            wi.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
                            sess.save(wi);
                            sess.flush();
                            channels.add(channel);
                            laneNumber++;
                        }
                        flowCell.setIdNumberSequencingCycles(idNumberSequencingCycles);
                        flowCell.setFlowCellChannels(channels);
                        String notes = "";
                        for (Iterator i = requestNumbers.keySet().iterator(); i.hasNext(); ) {
                            notes += i.next();
                            if (i.hasNext()) {
                                notes += ", ";
                            } else {
                                notes += " ";
                            }
                        }
                        if (idOrganisms.size() > 0) {
                            notes += "(";
                            for (Iterator i = idOrganisms.keySet().iterator(); i.hasNext(); ) {
                                notes += dh.getOrganism((Integer) i.next());
                                if (i.hasNext()) {
                                    notes += "/";
                                }
                            }
                            notes += ")";
                        }
                        if (!notes.equals("")) {
                            flowCell.setNotes(notes);
                        }
                        sess.save(flowCell);
                        sess.flush();
                        this.createFlowCellDirectory(flowCell, dh.getFlowCellDirectory(serverName));
                    }
                    if (this.dirtyWorkItemXMLString != null) {
                        for (Iterator i = this.parser.getDirtyWorkItemList().iterator(); i.hasNext(); ) {
                            WorkItem wi = (WorkItem) i.next();
                            sess.save(wi);
                        }
                        sess.flush();
                    }
                    parser.resetIsDirty();
                    XMLOutputter out = new org.jdom.output.XMLOutputter();
                    if (flowCell != null) {
                        this.xmlResult = "<SUCCESS flowCellNumber='" + flowCell.getNumber() + "'/>";
                    } else {
                        this.xmlResult = "<SUCCESS/>";
                    }
                    setResponsePage(this.SUCCESS_JSP);
                } else {
                    this.addInvalidField("Insufficient permissions", "Insufficient permission to manage workflow");
                    setResponsePage(this.ERROR_JSP);
                }
            } catch (Exception e) {
                log.error("An exception has occurred in SaveWorkflowSolexaAssemble ", e);
                e.printStackTrace();
                throw new RollBackCommandException(e.getMessage());
            } finally {
                try {
                    HibernateSession.closeSession();
                } catch (Exception e) {
                }
            }
        } else {
            this.xmlResult = "<SUCCESS/>";
            setResponsePage(this.SUCCESS_JSP);
        }
        return this;
    }
