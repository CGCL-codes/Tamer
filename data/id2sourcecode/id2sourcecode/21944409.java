        public void update(int width, int height) {
            TGMeasure measure = getMeasure();
            int track = measure.getTrack().getNumber();
            int numerator = measure.getTimeSignature().getNumerator();
            int denominator = measure.getTimeSignature().getDenominator().getValue();
            boolean percussion = TuxGuitar.instance().getSongManager().isPercussionChannel(measure.getTrack().getChannelId());
            if (width != this.width || height != this.height || this.track != track || this.numerator != numerator || this.denominator != denominator || this.percussion != percussion) {
                disposeBuffer();
            }
            this.track = track;
            this.numerator = numerator;
            this.denominator = denominator;
            this.percussion = percussion;
            this.width = width;
            this.height = height;
        }
