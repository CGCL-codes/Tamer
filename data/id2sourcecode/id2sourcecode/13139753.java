    void fillBuffer(float[] floatBuffer, int numberOfFrames, int channels) {
        ChannelControlMaster ccm = synth.getChannelControlMasterByPatch(patch);
        if (ccm != null) {
            ccm.fillBufferBeforeNotes(floatBuffer, numberOfFrames, channels);
        }
        while (newNotes.size() > 0) playingNotes.add(newNotes.remove(0));
        while (finishedNotes.size() > 0) playingNotes.remove(finishedNotes.remove(0));
        if (!playingNotes.isEmpty()) {
            for (int n = 0; n < midiChannelFloatBuffer.length; n++) midiChannelFloatBuffer[n] = 0;
            for (Note note : playingNotes) note.fillBuffer(midiChannelFloatBuffer, numberOfFrames, channels);
            for (int n = 0; n < midiChannelFloatBuffer.length; n++) {
                floatBuffer[n] += midiChannelFloatBuffer[n];
            }
        }
        if (ccm != null) {
            ccm.fillBufferAfterNotes(floatBuffer, numberOfFrames, channels);
        }
    }
