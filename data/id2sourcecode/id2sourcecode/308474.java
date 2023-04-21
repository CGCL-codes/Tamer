    public void callback() {
        try {
            double chan0 = (bs.getChannel(0) - X_center) / 4000.0;
            double chan1 = (bs.getChannel(1) - Y_center) / 4000.0;
            System.out.println("Chan 0 = " + chan0 + " : Chan 1 = " + chan1 + " : ");
            Event newEvent = new Event("iStuffEvent");
            newEvent.addField("Name", "Tilt");
            newEvent.addField("X.Axis.Min", new Double(-1));
            newEvent.addField("X.Axis.Max", new Double(1));
            newEvent.addField("X.Axis.Value", new Double(chan0));
            newEvent.addField("X.Axis.IsChanged", new Integer(1));
            newEvent.addField("Y.Axis.Min", new Double(-1));
            newEvent.addField("Y.Axis.Max", new Double(1));
            newEvent.addField("Y.Axis.Value", new Double(chan1));
            newEvent.addField("Y.Axis.IsChanged", new Integer(1));
            newEvent.addField("EHC_Timestamp", new Long(System.currentTimeMillis()));
            eheap.putEvent(newEvent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
