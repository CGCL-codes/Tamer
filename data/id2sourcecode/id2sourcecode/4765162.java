        private static final RGBBase.Channel[][] getXTalkChannelsSet() {
            RGBBase.Channel[][] channelsSet = new RGBBase.Channel[3][];
            for (int x = 0; x < 3; x++) {
                RGBBase.Channel ch0 = RGBBase.Channel.getChannelByArrayIndex(x);
                RGBBase.Channel ch1 = RGBBase.Channel.getChannelByArrayIndex((x + 1) % 3);
                if (ch0.getArrayIndex() < ch1.getArrayIndex()) {
                    channelsSet[x] = new RGBBase.Channel[] { ch0, ch1 };
                } else {
                    channelsSet[x] = new RGBBase.Channel[] { ch1, ch0 };
                }
            }
            return channelsSet;
        }
