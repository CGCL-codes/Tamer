        public long skip(long len) throws IOException {
            long ret = ais.skip((len / targetChannels) * sourceChannels);
            if (ret < 0) return ret;
            return (ret / sourceChannels) * targetChannels;
        }
