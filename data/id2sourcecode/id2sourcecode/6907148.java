    public void killNotesActionPerformed() {
        if (midiPlayer.isPlaying()) stop();
        ShortMessage noteOffMsg = new ShortMessage();
        try {
            ArrayList<Receiver> receiverArrayList = MIDIDeviceManager.getReceiverArrayList();
            Collection<Track> trackList = TrackHolder.getTracks();
            HashMap<Integer, Boolean> channelUpdateMap = new HashMap<Integer, Boolean>();
            for (Track tmpTrk : trackList) {
                if (!channelUpdateMap.containsKey(new Integer(tmpTrk.getChannel()))) {
                    for (Receiver receiver : receiverArrayList) {
                        for (int notePitch = 0; notePitch < 127; ++notePitch) {
                            noteOffMsg.setMessage(ShortMessage.NOTE_OFF, tmpTrk.getChannel(), notePitch, 0);
                            receiver.send(noteOffMsg, -1);
                        }
                    }
                    channelUpdateMap.put(new Integer(tmpTrk.getChannel()), new Boolean(true));
                }
            }
        } catch (Exception ex) {
            MsgHandler.error(ex);
        }
    }
