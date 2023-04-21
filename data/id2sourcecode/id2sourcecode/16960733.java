    public Vector getBoardAttachments() {
        Vector table = new Vector();
        AttachmentList boards = attachments.getAllOfType(Attachment.BOARD);
        Iterator i = boards.iterator();
        while (i.hasNext()) {
            BoardAttachment ba = (BoardAttachment) i.next();
            FrostBoardObject aBoard = ba.getBoardObj();
            Vector rows = new Vector();
            rows.add(aBoard.getBoardName());
            if (aBoard.getPublicKey() == null && aBoard.getPrivateKey() == null) {
                rows.add("public");
            } else if (aBoard.getPublicKey() != null && aBoard.getPrivateKey() == null) {
                rows.add("read - only");
            } else {
                rows.add("read / write");
            }
            if (aBoard.getDescription() == null) {
                rows.add("Not present");
            } else {
                rows.add(aBoard.getDescription());
            }
            table.add(rows);
        }
        return table;
    }
