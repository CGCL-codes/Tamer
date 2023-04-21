                        @Override
                        public void mouseReleased(MouseEvent arg0) {
                            if (client != null && selectedChannel != null && selectedChannel instanceof Channel) {
                                client.leave((Channel) selectedChannel);
                                Room[] channels = client.getClientState().getChannels();
                                channelList.setListData(channels);
                            }
                        }
