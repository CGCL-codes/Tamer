    private String readWrite() {
        switch(data.getReadWrite()) {
            case READ_WRITE:
                return "ReadWrite";
            case READ_ONLY:
                return "ReadOnly";
            case WRITE_ONLY:
                return "WriteOnly";
            default:
                break;
        }
        throw new RuntimeException("Invalid read-write type");
    }
