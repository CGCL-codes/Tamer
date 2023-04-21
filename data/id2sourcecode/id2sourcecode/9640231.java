        public final Builder setWorldWrite() {
            if (isReadOnly()) throw new IllegalStateException("Mount flag already set to readonly. Flag cannot indicate readonly and world write at the same time.");
            mountFlags |= 0x0008;
            return this;
        }
