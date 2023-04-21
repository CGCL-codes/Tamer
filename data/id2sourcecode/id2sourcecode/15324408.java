    private void outputDelayedNotes(long bufferStart_ppq, long bufferEnd_ppq) throws InvalidMidiDataException {
        long delayBufferStart = bufferStart_ppq - delay_ppq;
        long delayBufferEnd = bufferEnd_ppq - delay_ppq;
        if (delayBufferEnd < 0) return;
        if (delayBufferStart < 0) delayBufferStart = 0;
        System.out.print("Before delayed message map size: " + delayedMessageMap.size());
        SortedMap<Long, ArrayList<ShortMessage>> subMap = delayedMessageMap.subMap(delayBufferStart, delayBufferEnd);
        System.out.print("; sub map size: " + subMap.size());
        int channel = toTrack.getChannel();
        for (Long key : subMap.keySet()) {
            ArrayList<ShortMessage> tmpArrayList = subMap.get(key);
            for (ShortMessage msg : tmpArrayList) {
                msg.setMessage(msg.getCommand(), channel, msg.getData1(), msg.getData2());
                toTrack.addMidiMessage(key + delay_ppq, msg);
            }
        }
        subMap.clear();
        System.out.println("; after delayed message map size: " + delayedMessageMap.size());
    }
