    private void addToControllerMap(MidiEvent event) {
        try {
            ShortMessage shm = (ShortMessage) event.getMessage();
            if (shm.getCommand() == ShortMessage.CONTROL_CHANGE) {
                int ccKey = ((shm.getChannel() & 0xf) << 8) | (shm.getData1() & 0xff);
                SortedMap<Long, Integer> ccValues;
                ccValues = controllerMap.get(ccKey);
                if (ccValues == null) {
                    ccValues = new TreeMap<Long, Integer>();
                    controllerMap.put(ccKey, ccValues);
                }
                ccValues.put(event.getTick(), shm.getData2());
            }
        } catch (Exception e) {
        }
    }
