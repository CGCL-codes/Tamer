    private void removeFromControllerMap(MidiEvent event) {
        try {
            ShortMessage shm = (ShortMessage) event.getMessage();
            if (shm.getCommand() == ShortMessage.CONTROL_CHANGE) {
                int ccKey = ((shm.getChannel() & 0xf) << 8) | (shm.getData1() & 0xff);
                controllerMap.get(ccKey).remove(event.getTick());
            }
        } catch (Exception e) {
        }
    }
