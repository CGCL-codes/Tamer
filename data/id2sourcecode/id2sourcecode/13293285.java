    private void setPVText() {
        String scanpv1 = "RF phase scan PV: " + scanVariable.getChannelName() + "  " + connectionMap.get(cavAmpPVName) + "\n";
        String scanpv2 = "RF amplitude scan PV: " + scanVariableParameter.getChannelName() + "  " + connectionMap.get(cavPhasePVName) + "\n";
        Iterator itr = measuredValuesV.iterator();
        int i = 1;
        String mvpvs = "\n";
        while (itr.hasNext()) {
            String name = ((MeasuredValue) itr.next()).getChannelName();
            mvpvs += "BPM monitor PV " + (new Integer(i)).toString() + " : " + name + "  " + connectionMap.get(name) + "\n";
            i++;
        }
        thePVText = scanpv1 + scanpv2 + mvpvs;
    }
