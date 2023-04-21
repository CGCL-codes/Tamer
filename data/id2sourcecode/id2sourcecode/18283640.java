    public JPopupMenu getAutomationMenu(SoundLayer soundLayer) {
        this.selectedSoundLayer = soundLayer;
        JPopupMenu menu = new JPopupMenu();
        JMenu instrRoot = new JMenu("Instrument");
        Arrangement arrangement = data.getArrangement();
        ParameterIdList paramIdList = soundLayer.getAutomationParameters();
        for (int i = 0; i < arrangement.size(); i++) {
            InstrumentAssignment ia = arrangement.getInstrumentAssignment(i);
            if (ia.enabled && ia.instr instanceof Automatable) {
                ParameterList params = ((Automatable) ia.instr).getParameterList();
                if (params.size() <= 0) {
                    continue;
                }
                JMenu instrMenu = new JMenu();
                instrMenu.setText(ia.arrangementId + ") " + ia.instr.getName());
                for (int j = 0; j < params.size(); j++) {
                    Parameter param = params.getParameter(j);
                    JMenuItem paramItem = new JMenuItem();
                    paramItem.setText(param.getName());
                    paramItem.addActionListener(parameterActionListener);
                    if (param.isAutomationEnabled()) {
                        if (paramIdList.contains(param.getUniqueId())) {
                            paramItem.setForeground(Color.GREEN);
                        } else {
                            paramItem.setForeground(Color.ORANGE);
                        }
                    }
                    paramItem.putClientProperty("instr", ia.instr);
                    paramItem.putClientProperty("param", param);
                    instrMenu.add(paramItem);
                }
                instrRoot.add(instrMenu);
            }
        }
        menu.add(instrRoot);
        Mixer mixer = data.getMixer();
        if (mixer.isEnabled()) {
            JMenu mixerRoot = new JMenu("Mixer");
            ChannelList channels = mixer.getChannels();
            if (channels.size() > 0) {
                JMenu channelsMenu = new JMenu("Channels");
                for (int i = 0; i < channels.size(); i++) {
                    channelsMenu.add(buildChannelMenu(channels.getChannel(i), soundLayer));
                }
                mixerRoot.add(channelsMenu);
            }
            ChannelList subChannels = mixer.getSubChannels();
            if (subChannels.size() > 0) {
                JMenu subChannelsMenu = new JMenu("Sub-Channels");
                for (int i = 0; i < subChannels.size(); i++) {
                    subChannelsMenu.add(buildChannelMenu(subChannels.getChannel(i), soundLayer));
                }
                mixerRoot.add(subChannelsMenu);
            }
            Channel master = mixer.getMaster();
            mixerRoot.add(buildChannelMenu(master, soundLayer));
            menu.add(mixerRoot);
        }
        menu.addSeparator();
        JMenuItem clearAll = new JMenuItem("Clear All");
        clearAll.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Object retVal = DialogDisplayer.getDefault().notify(new NotifyDescriptor.Confirmation("Please Confirm Clearing All Parameter Data for this SoundLayer"));
                if (retVal == NotifyDescriptor.YES_OPTION) {
                    ParameterIdList idList = selectedSoundLayer.getAutomationParameters();
                    Iterator iter = new ArrayList(idList.getParameters()).iterator();
                    while (iter.hasNext()) {
                        String paramId = (String) iter.next();
                        Parameter param = getParameter(paramId);
                        param.setAutomationEnabled(false);
                        idList.removeParameterId(paramId);
                    }
                }
            }
        });
        menu.add(clearAll);
        clearAll.setEnabled(soundLayer.getAutomationParameters().size() > 0);
        return menu;
    }
