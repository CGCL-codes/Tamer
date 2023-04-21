        public ChannelPanel(GPChannelStack chs, AClip c, int channelIndex) {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            channelStack = chs;
            layer = c.getSelectedLayer();
            this.channelIndex = channelIndex;
            channel = layer.getChannel(channelIndex);
            audible = new JCheckBox();
            audible.setSelectedIcon(GToolkit.loadIcon(this, "audibleSelectedCheckBox"));
            audible.setIcon(GToolkit.loadIcon(this, "audibleUnselectedCheckBox"));
            audible.setToolTipText(GLanguage.translate("audible"));
            audible.setSelected(channel.isAudible());
            audible.addActionListener(this);
            add(audible);
            channelView = new GChannelViewer(getFocussedClip(), c.getSelectedIndex(), channelIndex);
            channelView.setPreferredSize(new Dimension(100, 30));
            channelView.addMouseListener(this);
            add(channelView);
            channelName = new JTextField(30);
            channelName.setToolTipText(GLanguage.translate("channelName"));
            channelName.setText(channel.getName());
            channelName.addActionListener(this);
            add(channelName);
            setBorder(BorderFactory.createEtchedBorder());
            setSelected(layer.getSelected() == channel);
            addMouseListener(this);
            setPreferredSize(new Dimension(340, 40));
        }
