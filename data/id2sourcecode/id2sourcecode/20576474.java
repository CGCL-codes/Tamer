    public void createPLLwrappers(String initPv) {
        Channel ch;
        String chName = initPv + ":DbgMPSPulseLossLimit";
        pllWrapper = new ChannelWrapper(chName);
        MPSpllWrap.add(pllWrapper);
        pllWrapper.addCAValueListener(this);
        String str;
        str = "" + pllWrapper.getValue();
        MPSpllMap.put(chName, str);
        try {
            ch = pllWrapper.getChannel();
            ch.putVal(1);
            ch.flushIO();
        } catch (ConnectionException e) {
            System.err.println("Unable to connect to channel access.");
        } catch (PutException e) {
            System.err.println("Unable to set process variables.");
        }
        chName = initPv + ":DbgCmdSetParameters";
        cspWrapper = new ChannelWrapper(chName);
        MPSblmWrap.add(cspWrapper);
        cspWrapper.addCAValueListener(this);
        try {
            ch = cspWrapper.getChannel();
            ch.putVal(1);
            ch.flushIO();
        } catch (ConnectionException e) {
            System.err.println("Unable to connect to channel access.");
        } catch (PutException e) {
            System.err.println("Unable to set process variables.");
        }
    }
