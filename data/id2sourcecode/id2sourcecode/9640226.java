        public final Builder setReadOnly() {
            if (isWorldWrite()) throw new IllegalStateException("Mount flag already set to worldwrite. Flag cannot indicate readonly and world write at the same time.");
            mountFlags |= 0x0001;
            return this;
        }
