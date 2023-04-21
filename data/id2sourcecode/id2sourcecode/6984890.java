    private void addToControllerMap(final MidiEvent i_event, final long i_tick) {
        try {
            if (i_event.getCommand() == ShortMessage.CONTROL_CHANGE) {
                int ccKey = ((i_event.getChannel() & 0xf) << 8) | (i_event.getData1() & 0xff);
                SortedMap ccValues;
                ccValues = (SortedMap) controllerMap.get(new Integer(ccKey));
                if (ccValues == null) {
                    ccValues = new TreeMap();
                    controllerMap.put(new Integer(ccKey), ccValues);
                }
                ccValues.put(new Long(i_tick), new Integer(i_event.getData2()));
            }
        } catch (Exception e) {
        }
    }
