    private void setTdValue(int val) {
        try {
            tdWrapper.getChannel().putVal(val);
        } catch (ConnectionException e) {
            System.err.println("Unable to connect to channel access.");
        } catch (PutException e) {
            System.err.println("Unable to set process variables.");
        }
    }
