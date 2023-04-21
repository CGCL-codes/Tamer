    public void openIO() throws IOException {
        initOpen(2);
        switch(accessMode) {
            case Constants.SEQUENTIAL_ACCESS:
                readAllowed = rewriteAllowed = startAllowed = deleteAllowed = true;
                break;
            case Constants.RANDOM_ACCESS:
                readAllowed = rewriteAllowed = writeAllowed = deleteAllowed = true;
                break;
            case Constants.DYNAMIC_ACCESS:
                readAllowed = rewriteAllowed = writeAllowed = startAllowed = deleteAllowed = true;
            default:
        }
        try {
            prepareDelete();
            prepareRewrite();
            prepareRead();
            deleteAllowed = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
