        public ValueListener(String pvName) {
            channel = ChannelFactory.defaultFactory().getChannel(pvName);
            try {
                monitor = channel.addMonitorValTime(this, Monitor.VALUE);
            } catch (ConnectionException e1) {
                fail("ConnectionException on: " + pvName);
            } catch (MonitorException e1) {
                fail("MonitorException on: " + pvName);
            }
        }
