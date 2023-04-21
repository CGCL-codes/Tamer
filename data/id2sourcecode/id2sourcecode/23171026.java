    public final void testProfileScan() {
        int intWaitTm = 2;
        String strSigName = "SCL_Diag:WS007:Command";
        String strMonName = "SCL_Diag:WS007:MotionStat";
        System.out.println();
        System.out.println();
        System.out.println("Testing Profile Scan using " + strSigName);
        System.out.println("  monitoring on PV " + strMonName);
        ChannelFactory factory = ChannelFactory.defaultFactory();
        Channel chanCmd = factory.getChannel(strSigName);
        Channel chanMon = factory.getChannel(strMonName);
        try {
            chanCmd.connectAndWait(10);
            chanMon.connectAndWait(10);
            JcaMonitorSink sink = new JcaMonitorSink();
            gov.sns.ca.Monitor monitor = chanMon.addMonitorValue(sink, Monitor.VALUE);
            chanCmd.putVal(3);
            System.out.println("  Scan started.");
            System.out.println("  Sleeping for " + intWaitTm + " minutes while waiting for monitor events");
            for (int iMinute = 1; iMinute <= intWaitTm; iMinute++) {
                Thread.sleep(60000);
                System.out.println("    " + iMinute + " minute(s)");
            }
            System.out.println("Awakened.  Returning from scan test");
            System.out.println("  (NOTE: scan may still be in progress)");
            monitor.clear();
        } catch (ConnectionException e) {
            System.err.println("ERROR: XAL Connection exception " + strSigName);
            e.printStackTrace();
            fail("ERROR: XAL Connection exception " + strSigName);
        } catch (PutException e) {
            System.err.println("ERROR: Profile Scan exception " + strSigName);
            e.printStackTrace();
            fail("ERROR: Profile Scann exception " + strSigName);
        } catch (MonitorException e) {
            System.err.println("ERROR: Profile Scan monitoring exception " + strMonName);
            e.printStackTrace();
            fail("ERROR: Profile Scann monitoring exception " + strMonName);
        } catch (InterruptedException e) {
            System.err.println("Scanning sleep thread interrupted while scanning");
            e.printStackTrace();
            fail("Scanning sleep thread interrupted while scanning");
        }
    }
