        public EditorNote(javax.sound.midi.MidiEvent onEvt, javax.sound.midi.MidiEvent offEvt, javax.sound.midi.Track track) {
            this.onEvt = onEvt;
            this.offEvt = offEvt;
            this.on = (javax.sound.midi.ShortMessage) this.onEvt.getMessage();
            this.off = (javax.sound.midi.ShortMessage) this.offEvt.getMessage();
            this.track = track;
            this.noteval = on.getData1();
            this.notestart = onEvt.getTick();
            this.notelen = offEvt.getTick() - onEvt.getTick();
            this.chan = on.getChannel();
            this.vel = on.getData2();
            this.offVel = off.getData2();
        }
