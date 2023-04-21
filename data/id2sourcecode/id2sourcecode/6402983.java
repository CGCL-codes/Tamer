    public static void probe(final BrowserPV pv, final String fieldName, final String fieldType) {
        pv.setProbeData(true);
        StringBuffer channelName = new StringBuffer(pv.getID());
        channelName.append(".");
        channelName.append(fieldName);
        String channelNameString = channelName.toString();
        final Channel channel = ChannelFactory.defaultFactory().getChannel(channelNameString);
        channel.addConnectionListener(new ConnectionListener() {

            public void connectionMade(Channel channel) {
                try {
                    channel.getValueCallback(new IEventSinkValue() {

                        public void eventValue(ChannelRecord record, Channel chan) {
                            String value = record.stringValue();
                            BrowserPVField newField = new BrowserPVField(fieldName, value, fieldType);
                            pv.addField(newField);
                        }
                    });
                    Channel.flushIO();
                } catch (ConnectionException e) {
                    Application.displayError(e);
                    e.printStackTrace();
                } catch (GetException e) {
                    Application.displayError(e);
                    e.printStackTrace();
                }
            }

            public void connectionDropped(Channel channel) {
            }
        });
        channel.requestConnection();
        Channel.flushIO();
    }
