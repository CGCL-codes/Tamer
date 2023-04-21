    public Command execute() throws RollBackCommandException {
        try {
            Session sess = HibernateSession.currentSession(this.getUsername());
            DictionaryHelper dh = DictionaryHelper.getInstance(sess);
            if (this.getSecurityAdvisor().canUpdate(fc)) {
                channelParser.parse(sess);
                FlowCell flowCell = null;
                if (isNewFlowCell) {
                    flowCell = fc;
                    sess.save(flowCell);
                } else {
                    flowCell = (FlowCell) sess.get(FlowCell.class, fc.getIdFlowCell());
                    initializeFlowCell(flowCell);
                }
                TreeSet channelsToDelete = new TreeSet(new ChannelComparator());
                for (Iterator i = flowCell.getFlowCellChannels().iterator(); i.hasNext(); ) {
                    FlowCellChannel existingChannel = (FlowCellChannel) i.next();
                    if (!channelParser.getChannelMap().containsKey(existingChannel.getIdFlowCellChannel().toString())) {
                        channelsToDelete.add(existingChannel);
                        List workItems = sess.createQuery("SELECT x from WorkItem x where idFlowCellChannel = " + existingChannel.getIdFlowCellChannel()).list();
                        for (Iterator i1 = workItems.iterator(); i1.hasNext(); ) {
                            WorkItem x = (WorkItem) i1.next();
                            sess.delete(x);
                        }
                        for (Iterator i2 = existingChannel.getSequenceLanes().iterator(); i2.hasNext(); ) {
                            SequenceLane lane = (SequenceLane) i2.next();
                            lane.setIdFlowCellChannel(null);
                        }
                    }
                }
                for (Iterator i = channelsToDelete.iterator(); i.hasNext(); ) {
                    FlowCellChannel channelToDelete = (FlowCellChannel) i.next();
                    flowCell.getFlowCellChannels().remove(channelToDelete);
                }
                String runFolder = flowCell.getRunFolderName(dh);
                java.sql.Date lastCycleDate = null;
                if (lastCycleDateStr != null) {
                    lastCycleDate = this.parseDate(lastCycleDateStr);
                }
                Integer numberSequencingCyclesActual = null;
                if (numberSequencingCyclesActualStr != null && numberSequencingCyclesActualStr.length() > 0) {
                    numberSequencingCyclesActual = new Integer(numberSequencingCyclesActualStr);
                }
                for (Iterator i = channelParser.getChannelMap().keySet().iterator(); i.hasNext(); ) {
                    String idFlowCellChannelString = (String) i.next();
                    FlowCellChannel fcc = (FlowCellChannel) channelParser.getChannelMap().get(idFlowCellChannelString);
                    if (runFolder != null) {
                        fcc.setFileName(runFolder);
                    }
                    fcc.setLastCycleDate(lastCycleDate);
                    fcc.setNumberSequencingCyclesActual(numberSequencingCyclesActual);
                    boolean exists = false;
                    for (Iterator i1 = flowCell.getFlowCellChannels().iterator(); i1.hasNext(); ) {
                        FlowCellChannel existingChannel = (FlowCellChannel) i1.next();
                        if (existingChannel.getIdFlowCellChannel().equals(fcc.getIdFlowCellChannel())) {
                            exists = true;
                        }
                    }
                    if (!exists) {
                        flowCell.getFlowCellChannels().add(fcc);
                    }
                }
                sess.flush();
                this.xmlResult = "<SUCCESS idFlowCell=\"" + flowCell.getIdFlowCell() + "\"/>";
                setResponsePage(this.SUCCESS_JSP);
            } else {
                this.addInvalidField("Insufficient permissions", "Insufficient permission to save flowCell.");
                setResponsePage(this.ERROR_JSP);
            }
        } catch (Exception e) {
            log.error("An exception has occurred in SaveFlowCell ", e);
            e.printStackTrace();
            throw new RollBackCommandException(e.getMessage());
        } finally {
            try {
                HibernateSession.closeSession();
            } catch (Exception e) {
            }
        }
        return this;
    }
