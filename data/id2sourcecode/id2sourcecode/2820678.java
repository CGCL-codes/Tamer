            public void actionPerformed(ActionEvent e) {
                final ConsoleSpec spec = (ConsoleSpec) consolesList.getSelectedValue();
                if (spec == null) return;
                ConsoleManager consoleManager = CustomConsolesPrefsPanel.this.consoleManager;
                SortedMap channels = new TreeMap(consoleManager.getChannels());
                channels.values().removeAll(spec.getChannels());
                Channel[] channelsArr = (Channel[]) channels.values().toArray(new Channel[channels.size()]);
                final ChannelsPopup popup = new ChannelsPopup(channelsArr);
                popup.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        popup.hide();
                        Channel channel = popup.getSelectedChannel();
                        spec.addChannel(channel);
                        updateUiFromSelectedConsole();
                        fireStateChanged();
                    }
                });
                popup.show(addRemoveChannels, 0, addRemoveChannels.getHeight());
            }
