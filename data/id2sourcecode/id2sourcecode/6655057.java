    public MidiMessage processMessage(FrinikaTrackWrapper track, MidiPlayOptions opt, ShortMessage message) {
        if (opt == null) return message;
        byte[] msgBytes = message.getMessage();
        int ch = message.getChannel();
        if (track.getMidiChannel() != FrinikaTrackWrapper.CHANNEL_FROM_EVENT) {
            ch = track.getMidiChannel();
        }
        if ((msgBytes.length > 2) && (((msgBytes[0] & 0xf0) == ShortMessage.NOTE_OFF || (msgBytes[0] & 0xf0) == ShortMessage.NOTE_ON) && ((opt.transpose != 0) || (opt.velocityOffset != 0) || (opt.velocityCompression != 0.0f)))) {
            int note = msgBytes[1];
            note += opt.transpose;
            if (note < 0) {
                note = 0;
            } else if (note > 127) {
                note = 127;
            }
            int vel = msgBytes[2];
            if (vel != 0) {
                if (opt.velocityCompression != 0.0f) {
                    float diff = (64 - vel) * opt.velocityCompression;
                    vel += diff;
                }
                vel += opt.velocityOffset;
                if (vel < 1) {
                    vel = 1;
                } else if (vel > 127) {
                    vel = 127;
                }
            }
            ShortMessage shm = new ShortMessage();
            try {
                shm.setMessage(message.getCommand(), ch, note, vel);
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }
            return shm;
        } else {
            ShortMessage shm = new ShortMessage();
            try {
                shm.setMessage(message.getCommand(), ch, message.getData1(), message.getData2());
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }
            return shm;
        }
    }
