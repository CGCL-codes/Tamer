    private void setPVText() {
        String scanpv1 = "RF phase scan PV: " + scanVariable.getChannelName() + "\n";
        scanpv1 += "RF phase strobe PV: " + scanVariable.getStrobeChan().channelName() + "\n";
        String scanpv2 = "RF amplitude scan PV: " + scanVariableParameter.getChannelName() + "\n";
        scanpv2 += "RF amplitude strobe PV: " + scanVariableParameter.getStrobeChan().channelName() + "\n";
        Iterator itr = measuredValuesV.iterator();
        int i = 1;
        String mvpvs = "\n";
        while (itr.hasNext()) {
            String name = ((MeasuredValue) itr.next()).getChannelName();
            mvpvs += "Monitor PV " + (new Integer(i)).toString() + ": " + name + "\n";
            i++;
        }
        Set connectSet = connectionMap.entrySet();
        Iterator connectItr = connectSet.iterator();
        String connects = "\nTotal channels: " + connectionMap.size();
        connects += "\nConnection Status:\n";
        while (connectItr.hasNext()) {
            Map.Entry me = (Map.Entry) connectItr.next();
            Boolean tf = (Boolean) me.getValue();
            connects += me.getKey().toString() + ": " + tf.toString() + "\n";
        }
        thePVText = scanpv1 + scanpv2 + mvpvs + connects;
    }
