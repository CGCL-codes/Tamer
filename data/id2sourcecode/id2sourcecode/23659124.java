    protected synchronized void drawProgrammes() {
        currentProgrammeLabel = null;
        JLabelProgramme.setupLabel(this);
        DateFormat timeFormat = getCurrentDateFormat();
        final Font font = new Font(config.fontName, config.fontStyle, config.fontSize);
        panel.setHTMLFont(font);
        final TVChannelsSet currentChannelSet = (TVChannelsSet) getChannelsSetByName(config.currentChannelSetName).clone();
        Iterator it = currentChannelSet.getChannels().iterator();
        while (it.hasNext()) {
            TVChannelsSet.Channel listCh = (TVChannelsSet.Channel) it.next();
            if (!currentData.containsChannel(listCh.getChannelID())) {
                it.remove();
            }
        }
        panel.getProgrammesPanel().init(getDate(), font, currentChannelSet.getChannels().size(), timeFormat);
        final List channels = new ArrayList();
        for (it = currentChannelSet.getChannels().iterator(); it.hasNext(); ) {
            TVChannelsSet.Channel listCh = (TVChannelsSet.Channel) it.next();
            TVChannel curChan = currentData.get(listCh.getChannelID());
            channels.add(curChan);
        }
        panel.getChannelNamePanel().setFont(font);
        panel.getChannelNamePanel().setChanels((TVChannel[]) channels.toArray(new TVChannel[channels.size()]));
        panel.getChannelNamePanel().setPreferredSize(new Dimension(panel.getChannelNamePanel().getMaxChannelWidth(), (currentChannelSet.getChannels().size() * config.sizeChannelHeight) + 50));
        Dimension tmp = new Dimension((int) (config.sizeProgrammeHour * todayMillis / Time.HOUR), currentChannelSet.getChannels().size() * config.sizeChannelHeight);
        panel.getProgrammesPanel().setPreferredSize(tmp);
        panel.getProgrammesPanel().setMinimumSize(tmp);
        panel.getProgrammesPanel().setMaximumSize(tmp);
        tmp = new Dimension((int) (config.sizeProgrammeHour * todayMillis / Time.HOUR), panel.getTimePanel().getPreferredSize().height);
        panel.getTimePanel().setPreferredSize(tmp);
        panel.getTimePanel().setMinimumSize(tmp);
        panel.getTimePanel().setMaximumSize(tmp);
        panel.getTimePanel().setTimes(getDate(), getDate() + todayMillis);
        currentData.iterate(new TVIteratorProgrammes() {

            protected void onChannel(TVChannel channel) {
            }

            public void onProgramme(TVProgramme programme) {
                int row = currentChannelSet.getChannelIndex(getCurrentChannel().getID());
                if (row != -1) {
                    panel.getProgrammesPanel().addProgramme(programme, row);
                }
            }
        });
        panel.getProgrammesPanel().sort();
        panel.getTimePanel().revalidate();
        panel.getTimePanel().repaint();
        panel.getProgrammesPanel().revalidate();
        panel.getProgrammesPanel().repaint();
        panel.getChannelNamePanel().revalidate();
        panel.getChannelNamePanel().repaint();
    }
