    public static final String toString(byte flag) {
        switch(flag) {
            case NONE:
                return "none";
            case CAN_READ:
                return "can read";
            case CAN_WRITE:
                return "can write";
            case CAN_READ_WRITE:
                return "can read and write";
            default:
                throw new RuntimeException("Unrecognized flag: " + flag);
        }
    }
