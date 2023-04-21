        public static final Channel getChannel(int chIndex) {
            switch(chIndex) {
                case 1:
                    return Channel.R;
                case 2:
                    return Channel.G;
                case 3:
                    return Channel.B;
                case 4:
                    return Channel.Y;
                case 5:
                    return Channel.M;
                case 6:
                    return Channel.C;
                case 7:
                    return Channel.W;
                default:
                    return null;
            }
        }
