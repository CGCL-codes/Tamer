    private void emphasiseNotes(long beat_ppq, int velocityPercentageChange, Track midiTrack) throws InvalidMidiDataException {
        for (ShortMessage tempShortMsg : midiTrack.getMidiMessages(beat_ppq)) {
            if (tempShortMsg.getCommand() == ShortMessage.NOTE_ON) {
                int newVelocity = tempShortMsg.getData2() * velocityPercentageChange / 100;
                if (newVelocity > maxVelocity) newVelocity = maxVelocity; else if (newVelocity < minVelocity) newVelocity = minVelocity;
                tempShortMsg.setMessage(ShortMessage.NOTE_ON, midiTrack.getChannel(), tempShortMsg.getData1(), newVelocity);
            }
        }
    }
