            public void run() {
                TGTrack tgTrack = getMeasure().getTrack();
                TGChannel tgChannel = TuxGuitar.instance().getSongManager().getChannel(tgTrack.getChannelId());
                if (tgChannel != null) {
                    int volume = TGChannel.DEFAULT_VOLUME;
                    int balance = TGChannel.DEFAULT_BALANCE;
                    int chorus = tgChannel.getChorus();
                    int reverb = tgChannel.getReverb();
                    int phaser = tgChannel.getPhaser();
                    int tremolo = tgChannel.getTremolo();
                    int channel = tgChannel.getChannelId();
                    int program = tgChannel.getProgram();
                    int bank = tgChannel.getBank();
                    int[][] beat = new int[][] { new int[] { (tgTrack.getOffset() + value), TGVelocities.DEFAULT } };
                    TuxGuitar.instance().getPlayer().playBeat(channel, bank, program, volume, balance, chorus, reverb, phaser, tremolo, beat);
                }
            }
