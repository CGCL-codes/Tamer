    private void fireRestores() {
        ScoreRecord record;
        Double spVal;
        ChannelWrapper channel;
        ChannelWrapper stbChannel;
        final int strobeTimeOut = 10;
        String name;
        badPVs.clear();
        badValuePVs.clear();
        glitchOccurred = false;
        Iterator itr = selectedRecords.iterator();
        while (itr.hasNext()) {
            record = (ScoreRecord) itr.next();
            channel = record.getSPChannel();
            stbChannel = null;
            if (record.getSTBChannel() != null) {
                stbChannel = record.getSTBChannel();
            }
            Integer stbVal = (Integer) record.valueForKey(PVData.stbValKey);
            if (channel != null && channel.isConnected()) {
                name = channel.getId();
                spVal = (Double) record.valueForKey(PVData.spSavedValKey);
                if (restoreFlag == restoreFromSavedRB) {
                    spVal = (Double) record.valueForKey(PVData.rbSavedValKey);
                }
                if (spVal.isNaN() || spVal.doubleValue() == -1000000) {
                    glitchOccurred = true;
                    badValuePVs.add(name);
                }
                if (spVal != null && !spVal.isNaN() && (spVal.doubleValue() != -1000000)) {
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
                                        if (stbChannel == null) {
                                            channel.getChannel().putValCallback(spVal.doubleValue(), this);
                                            if (restoreFlag == restoreFromSavedRB) {
                                                System.out.println("restore " + name + " from saved RB, value = " + spVal.doubleValue());
                                            }
                                        } else {
                                            channel.getChannel().putValCallback(spVal.doubleValue(), this);
                                            if (restoreFlag == restoreFromSavedRB) {
                                                System.out.println("restore " + name + " from saved RB, value = " + spVal.doubleValue());
                                            }
                                            Thread.sleep(strobeTimeOut);
                                            System.out.println("setting strobe " + stbChannel.getId() + " to " + stbVal.intValue());
                                            stbChannel.getChannel().putValCallback(stbVal.intValue(), this);
                                        }
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
            } else {
                System.out.println("bad channel = " + channel.getId());
                if (channel != null) badPVs.add(channel.getId());
            }
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
