        public final Channel getChannel(boolean R, boolean G, boolean B) {
            int index = 0;
            index += R ? 1 : 0;
            index += G ? 2 : 0;
            index += B ? 3 : 0;
            return getChannel(index);
        }
