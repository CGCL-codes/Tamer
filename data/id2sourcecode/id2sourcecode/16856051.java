        public CounterAllEvent(Component target, long[] values, long eventMoment) {
            super(target, IOEvent.ID_COUNTER_ALL, eventMoment);
            if (values.length != IOChannels.COUNTER.getChannelCount()) throw new IllegalArgumentException("wrong array size");
            this.values = values;
        }
