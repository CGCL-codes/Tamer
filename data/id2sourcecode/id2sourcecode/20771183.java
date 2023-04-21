    private void fireRestores() {
        ScoreRecord record;
        Double spVal;
        ChannelWrapper channel;
        String name;
        Boolean useRBVal;
        badPVs.clear();
        badValuePVs.clear();
        glitchOccurred = false;
        Iterator itr = selectedRecords.iterator();
        while (itr.hasNext()) {
            record = (ScoreRecord) itr.next();
            channel = record.getSPChannel();
            if (channel != null && channel.isConnected()) {
                name = channel.getId();
                spVal = (Double) record.valueForKey(PVData.spSavedValKey);
                useRBVal = (Boolean) record.valueForKey(PVData.restoreRBValKey);
                if (useRBVal != null) if (useRBVal.booleanValue()) spVal = (Double) record.valueForKey(PVData.rbSavedValKey);
                if (spVal.isNaN()) {
                    glitchOccurred = true;
                    badValuePVs.add(name);
                }
                if (spVal != null && !spVal.isNaN()) {
                    try {
                        if (channel.getChannel().writeAccess() == false) {
                            System.err.println("No WRITE access to " + name);
                            glitchOccurred = true;
                        } else {
                            try {
                                synchronized (sentChannels) {
                                    if (sentChannels.contains(name)) {
                                        glitchOccurred = true;
                                        System.err.println("Tried to set " + name + " more than once! only will do the first attempt");
                                    } else {
                                        channel.getChannel().putValCallback(spVal.doubleValue(), this);
                                        sentChannels.add(name);
                                    }
                                }
                            } catch (Exception evt) {
                                evt.printStackTrace();
                            }
                        }
                    } catch (ConnectionException eyah) {
                        System.err.println("PV " + name + " is not connected, will ignore this restore request");
                        glitchOccurred = true;
                    }
                }
            } else if (channel != null) badPVs.add(channel.getId());
        }
        Channel.flushIO();
        if (badPVs.size() > 0) {
            System.err.println(" The following setpoint PVs cannot be connected to and will not be restored:");
            glitchOccurred = true;
            Iterator itrBad = badPVs.iterator();
            while (itrBad.hasNext()) {
                name = (String) itrBad.next();
                System.err.println(name);
            }
        }
    }
