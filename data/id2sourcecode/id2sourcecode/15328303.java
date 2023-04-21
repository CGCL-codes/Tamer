    private int getChannelsPerMode(int mode) {
        switch(mode) {
            case 0:
                return 1;
            case 1:
                return 1;
            case 2:
                return -1;
            case 3:
                return 3;
            case 4:
                return 4;
            case 7:
                return -1;
            case 8:
                return -1;
            case 9:
                return 4;
            default:
                return -1;
        }
    }
