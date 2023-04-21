        public boolean getChannelSync() {
            if (numChannels == 0) return true;
            final boolean first = trackMap[0];
            for (int i = 1; i < numChannels; i++) {
                if (trackMap[i] != first) return false;
            }
            return true;
        }
