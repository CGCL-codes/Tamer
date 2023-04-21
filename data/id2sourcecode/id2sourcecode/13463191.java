        public static DigitalOutput getChannelForIndex(int channelNo) {
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
                case 6:
                    return CHANNEL6;
                case 7:
                    return CHANNEL7;
                case 8:
                    return CHANNEL8;
                default:
                    throw new IllegalArgumentException();
            }
        }
