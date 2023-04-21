        public int compare(Channel a, Channel b) {
            long diff = a.getChannelNumber() - b.getChannelNumber();
            if (diff < 0) {
                return -1;
            } else if (diff == 0) {
                return 0;
            } else {
                return 1;
            }
        }
