    protected void fireSignals() {
        if (!confirmed) {
            Toolkit.getDefaultToolkit().beep();
            String errText = "Whoa there - confirm action first";
            if (myWindow() != null) myWindow().textArea.setText(errText);
            System.err.println(errText);
            return;
        }
        channelManager.checkConnections(thePVList);
        Iterator itr = thePVList.iterator();
        String action = (String) myWindow().actionChoice.getSelectedItem();
        oldValues.clear();
        while (itr.hasNext()) {
            String name = (String) itr.next();
            Channel chan = ChannelFactory.defaultFactory().getChannel(name);
            if (chan.isConnected()) {
                try {
                    if (!setPreselected) {
                        double newVal = myWindow().valueField.getValue();
                        double oldVal = chan.getValDbl();
                        oldValues.put(name, new Double(oldVal));
                        if (action.startsWith("Incr")) newVal += oldVal;
                        if (action.startsWith("Mul")) newVal *= oldVal;
                        if (usePhaseWrap) newVal = wrapPhase(newVal);
                        chan.putVal(newVal);
                    } else {
                        String newVal = (String) myWindow().valueList.getSelectedValue();
                        byte[] val = chan.getArrByte();
                        oldValues.put(name, new String(val));
                        chan.putVal(newVal.getBytes());
                    }
                } catch (Exception ex) {
                    System.out.println("exception putting channel :" + chan.getId());
                }
            }
        }
    }
