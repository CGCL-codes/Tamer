    public void setData(List boardAttachments) {
        setRowCount(0);
        Iterator boards = boardAttachments.iterator();
        while (boards.hasNext()) {
            BoardAttachment attachment = (BoardAttachment) boards.next();
            Board board = attachment.getBoardObj();
            Object[] row = new Object[3];
            if (board.getName() != null) {
                row[0] = board.getName();
                if (board.getPublicKey() == null && board.getPrivateKey() == null) {
                    row[1] = "public";
                } else if (board.getPublicKey() != null && board.getPrivateKey() == null) {
                    row[1] = "read - only";
                } else {
                    row[1] = "read / write";
                }
                if (board.getDescription() == null) {
                    row[2] = "Not present";
                } else {
                    row[2] = board.getDescription();
                }
                addRow(row);
            }
        }
    }
