    public void mouseReleased(MouseEvent e) {
        if (press == null) return;
        SequenceView view = (SequenceView) e.getSource();
        if (e.getSource() == press.getSource()) {
            try {
                MidiSequence sequence = view.getSequence();
                long releaseTick = view.tick(e.getX(), e.getY());
                if (releaseTick <= press.getTick()) {
                    releaseTick = press.getTick() + (sequence.getResolution() / 32);
                }
                MidiTrack track = view.getTopTrack();
                int chan = track.getChannel();
                int velocity = editor.getDefaultVelocity(view);
                MidiMessage on = on(chan, press.getValue(), velocity);
                MidiMessage off = off(chan, press.getValue());
                MidiNote note = new MidiNote(new MidiEvent(on, view.snap(press.getTick())), new MidiEvent(off, releaseTick));
                TrackSelection notesel = new TrackSelection(track, note);
                edit(new Paste(notesel));
            } catch (InvalidMidiDataException ex) {
                ex.printStackTrace();
            } catch (CloneNotSupportedException cnse) {
            }
        }
        press = null;
    }
