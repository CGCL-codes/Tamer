        protected Info(Trail trail) {
            this.trail = trail;
            if (trail instanceof AudioTrail) {
                numChannels = ((AudioTrail) trail).getChannelNum();
            } else {
                numChannels = 1;
            }
            trackMap = new boolean[numChannels];
        }
