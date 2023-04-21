    private static void setTdValue(int val) {
        Channel td;
        try {
            td = tdWrapper.getChannel();
            td.putVal(val);
            td.flushIO();
        } catch (ConnectionException e) {
            System.err.println("Unable to connect to channel access.");
        } catch (PutException e) {
            System.err.println("Unable to set process variables.");
        }
    }
