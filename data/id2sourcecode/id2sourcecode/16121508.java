    public SharedFileObject(File file, FrostBoardObject board) {
        SHA1 = Core.getCrypto().digest(file);
        size = new Long(file.length());
        filename = file.getName();
        date = DateFun.getDate();
        this.file = file;
        key = null;
        this.board = board;
        if (board == null) batch = null; else {
            Iterator it = Core.getMyBatches().entrySet().iterator();
            while (it.hasNext()) {
                String current = (String) it.next();
                int size = ((Integer) Core.getMyBatches().get(current)).intValue();
                if (size < Core.frostSettings.getIntValue("batchSize")) {
                    batch = current;
                    break;
                }
            }
        }
    }
