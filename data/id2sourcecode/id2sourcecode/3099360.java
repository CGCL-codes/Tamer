    private void createTrack(MidiSequenceHandler sequence, TGTrack track) {
        TGMeasure previous = null;
        MidiRepeatController controller = new MidiRepeatController(track.getSong());
        addBend(sequence, track.getNumber(), TGDuration.QUARTER_TIME, DEFAULT_BEND, track.getChannel().getChannel());
        makeChannel(sequence, track.getChannel(), track.getNumber());
        while (!controller.finished()) {
            TGMeasure measure = track.getMeasure(controller.getIndex());
            int index = controller.getIndex();
            long move = controller.getRepeatMove();
            controller.process();
            if (controller.shouldPlay()) {
                if (track.getNumber() == 1) {
                    addTimeSignature(sequence, measure, previous, move);
                    addTempo(sequence, measure, previous, move);
                    addMetronome(sequence, measure.getHeader(), move);
                }
                makeBeats(sequence, track, measure, index, move);
            }
            previous = measure;
        }
    }
