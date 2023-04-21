    public String getActions() {
        switch(action_mask) {
            case ALL:
                return "read,write";
            case READ:
                return "read";
            case WRITE:
                return "write";
            default:
                return "";
        }
    }
