    public final void testXalMonitor() {
        String strPvName = "SCL_Diag:WS007:Ver_point_sig";
        System.out.println();
        System.out.println();
        System.out.println("Testing XAL monitor on PV " + strPvName);
        ChannelFactory factory = ChannelFactory.defaultFactory();
        Channel chan = factory.getChannel(strPvName);
        try {
            JcaMonitorSink sink = new JcaMonitorSink();
            gov.sns.ca.Monitor monitor = chan.addMonitorValue(sink, Monitor.VALUE);
        } catch (ConnectionException e) {
            System.err.println("ERROR: XAL Connection exception " + strPvName);
            e.printStackTrace();
            fail("ERROR: XAL Connection exception " + strPvName);
        } catch (MonitorException e) {
            System.err.println("ERROR: XAL monitor exception " + strPvName);
            e.printStackTrace();
            fail("ERROR: XAL monitor exception " + strPvName);
        }
    }
