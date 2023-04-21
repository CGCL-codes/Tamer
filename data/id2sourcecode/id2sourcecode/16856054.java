        public DigitalAllEvent(Component target, boolean[] values, long eventMoment) {
            super(target, IOEvent.ID_DIGITAL_ALL, eventMoment);
            if (values.length != IOChannels.DIGITAL.getChannelCount()) throw new IllegalArgumentException("wrong array size");
            this.values = values;
        }
