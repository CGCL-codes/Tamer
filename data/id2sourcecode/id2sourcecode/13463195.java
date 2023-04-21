        public static DigitalInput getChannelForIndex(int channelNo) {
            switch(channelNo) {
                case 1:
                    return CHANNEL1;
                case 2:
                    return CHANNEL2;
                case 3:
                    return CHANNEL3;
                case 4:
                    return CHANNEL4;
                case 5:
                    return CHANNEL5;
                default:
                    throw new IllegalArgumentException();
            }
        }
