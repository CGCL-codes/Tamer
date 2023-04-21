            public void actionPerformed(ActionEvent e) {
                final ConsoleSpec spec = (ConsoleSpec) consolesList.getSelectedValue();
                if (spec == null) return;
                Channel[] channelsArr = (Channel[]) spec.getChannels().toArray(new Channel[0]);
                final ChannelsPopup popup = new ChannelsPopup(channelsArr);
                popup.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        popup.hide();
                        Channel channel = popup.getSelectedChannel();
                        spec.removeChannel(channel);
                        updateUiFromSelectedConsole();
                        fireStateChanged();
                    }
                });
                popup.show(addRemoveChannels, 0, addRemoveChannels.getHeight());
            }
