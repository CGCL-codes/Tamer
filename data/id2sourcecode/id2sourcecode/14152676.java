    public static boolean nodeCanConnect(BLM blm) {
        boolean canConnect = true;
        try {
            canConnect = blm.getChannel(BLM.LOSS_AVG_HANDLE).connectAndWait();
            System.out.println(blm.getId() + " is Connected");
        } catch (NoSuchChannelException excpt) {
            canConnect = false;
            System.out.println(blm.getId() + " Can't connect");
        }
        return canConnect;
    }
