    private void removeFromControllerMap(final MidiEvent i_event, final long i_tick) {
        try {
            if (i_event.getCommand() == ShortMessage.CONTROL_CHANGE) {
                int ccKey = ((i_event.getChannel() & 0xf) << 8) | (i_event.getData1() & 0xff);
                ((SortedMap) controllerMap.get(new Integer(ccKey))).remove(new Long(i_tick));
            }
        } catch (Exception e) {
        }
    }
