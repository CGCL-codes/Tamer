    @Test
    public final void testXalChannel() {
        String strPvNmGet = "HEBT_Diag:WS09:Hor_trace_raw";
        String strPvNmSet = "SCL_Diag:WS007:Scan_InitialMove_set";
        System.out.println();
        System.out.println();
        System.out.println("Testing XAL connection to PV " + strPvNmGet);
        ChannelFactory factory = ChannelFactory.defaultFactory();
        Channel chan = factory.getChannel(strPvNmGet);
        try {
            int intVal = chan.getValInt();
            System.out.println("The current value of " + strPvNmGet + " = " + intVal);
            intVal = chan.lowerControlLimit().intValue();
            System.out.println("The current lower display limit is " + intVal);
            intVal = chan.upperControlLimit().intValue();
            System.out.println("The current upper display limit is " + intVal);
            Channel chput = factory.getChannel(strPvNmSet);
            chput.putVal(15);
        } catch (ConnectionException e) {
            System.err.println("ERROR: Unable to connect to " + strPvNmGet);
            e.printStackTrace();
            fail("ERROR: Unable to connect to " + strPvNmGet);
        } catch (GetException e) {
            System.err.println("ERROR: general caget exception for " + strPvNmGet);
            e.printStackTrace();
            fail("ERROR: general caget exception for " + strPvNmGet);
        } catch (PutException e) {
            e.printStackTrace();
        }
    }
