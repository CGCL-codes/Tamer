    public boolean RBNBChannel() {
        boolean returnVal = true;
        boolean newChans = false;
        RegChannel[] oldcha = null;
        PlotContainer[][] oldpca = null;
        Map imap = null;
        Channel[] chanInfo = null;
        if (connection == null) return false;
        try {
            if (rtReqMode) setRTReqMode(false);
            try {
                String[][] chanList = connection.getChannelList("*");
                cmap = new Map(chanList);
            } catch (Exception e) {
                System.err.println("RBNBInterface.RBNBChannel: exception");
                e.printStackTrace();
                return false;
            }
            channel = cmap.channelList();
            for (int i = 0; i < channel.length; i++) {
                if (channel[i].getChannelName().indexOf("_Log") != -1) {
                    cmap.removeChannel(channel[i]);
                }
            }
            channel = cmap.channelList();
            if (runMode == RunModeDefs.realTime) setRTReqMode(true);
            if (cha.length > 0) {
                oldcha = cha;
                oldpca = pca;
            } else newChans = true;
            cha = new RegChannel[channel.length];
            pca = new PlotContainer[environment.DISPLAYGROUPS][channel.length];
            for (int i = 0; i < channel.length; i++) {
                cha[i] = new RegChannel(channel[i]);
                for (int j = 0; j < environment.DISPLAYGROUPS; j++) pca[j][i] = null;
            }
            if (oldcha != null) {
                boolean[] kept = new boolean[oldcha.length];
                for (int i = 0; i < oldcha.length; i++) kept[i] = false;
                for (int i = 0; i < cha.length; i++) {
                    for (int j = 0; j < oldcha.length; j++) {
                        if (cha[i].name.equals(oldcha[j].name)) {
                            for (int k = 0; k < environment.DISPLAYGROUPS; k++) {
                                pca[k][i] = oldpca[k][j];
                            }
                            kept[j] = true;
                        }
                    }
                }
                for (int i = 0; i < oldcha.length; i++) if (!kept[i]) {
                    for (int j = 0; j < environment.DISPLAYGROUPS; j++) {
                        if (oldpca[j][i] != null) {
                            plotsContainer.removePlot(oldpca[j][i], j);
                            map[j].removeChannel(map[j].findChannel(oldcha[i].name));
                        }
                    }
                }
            }
            rbnbCubby.setAvailableChannels(cha);
            if (newChans) {
                String[] dgName = new String[channel.length];
                String[] dgLabel = new String[environment.DISPLAYGROUPS];
                for (int i = 0; i < channel.length; i++) {
                    if (channel[i].channelUserDataType == 1) {
                        KeyValueHash kvh = new KeyValueHash(channel[i].channelUserData);
                        dgName[i] = (String) kvh.get("group");
                    } else dgName[i] = null;
                }
                int j = 0;
                for (int i = 0; i < channel.length; i++) {
                    if (dgName[i] != null) {
                        boolean found = false;
                        for (int k = 0; k < j; k++) {
                            if (dgName[i].equals(dgLabel[k])) {
                                pca[k][i] = plotsContainer.addPlot(cha[i], k);
                                pca[k][i].setAbscissa(duration[dg]);
                                map[k].addChannel(cmap.findChannel(cha[i].name));
                                found = true;
                                k = j;
                            }
                        }
                        if (!found) {
                            dgLabel[j] = dgName[i];
                            pca[j][i] = plotsContainer.addPlot(cha[i], j);
                            pca[j][i].setAbscissa(duration[dg]);
                            map[j].addChannel(cmap.findChannel(cha[i].name));
                            j++;
                        }
                    }
                }
                int num = 0;
                if (environment.SHOWALLCHANNELS) {
                    for (int i = 0; i < channel.length; i++) {
                        if (j >= environment.DISPLAYGROUPS) break;
                        if (dgName[i] == null) {
                            pca[j][i] = plotsContainer.addPlot(cha[i], j);
                            pca[j][i].setAbscissa(duration[dg]);
                            map[j].addChannel(cmap.findChannel(cha[i].name));
                            if (++num % environment.CHANSPERDG == 0) j++;
                        }
                    }
                }
                plotsContainer.labelDisplayGroups(dgLabel);
            }
            int j = 0;
            for (int i = 0; i < cha.length; i++) if (pca[dg][i] != null) j++;
            RegChannel[] sca = new RegChannel[j];
            j = 0;
            for (int i = 0; i < cha.length; i++) if (pca[dg][i] != null) sca[j++] = cha[i];
            rbnbCubby.setSelectedChannels(sca, false);
            plotsContainer.invalidate();
            plotsContainer.validate();
            plotsContainer.repaint();
        } catch (Exception e) {
            System.out.println("RBNBInterface.RBNBChannel: caught " + e);
            e.printStackTrace();
            runMode = RunModeDefs.stop;
            if (rtReqMode) setRTReqMode(false);
            runModeCubby.set(RunModeDefs.stop, false);
            returnVal = false;
        }
        return returnVal;
    }
