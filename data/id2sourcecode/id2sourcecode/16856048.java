        public AnalogAllEvent(Component target, short[] values, long eventMoment) {
            super(target, IOEvent.ID_ANALOG_ALL, eventMoment);
            if (values.length != IOChannels.ANALOG.getChannelCount()) throw new IllegalArgumentException();
            this.values = values;
        }
