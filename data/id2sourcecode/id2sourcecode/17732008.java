    protected void writeLettersList(ByteBuffer buf, Collection<Letter> letters, Player player) {
        writeC(buf, 2);
        writeD(buf, player.getObjectId());
        writeC(buf, 0);
        writeH(buf, (0 - player.getMailbox().size()));
        for (Letter letter : letters) {
            writeD(buf, letter.getObjectId());
            writeS(buf, letter.getSenderName());
            writeS(buf, letter.getTitle());
            if (letter.isUnread()) writeC(buf, 0); else writeC(buf, 1);
            if (letter.getAttachedItem() != null) {
                writeD(buf, letter.getAttachedItem().getObjectId());
                writeD(buf, letter.getAttachedItem().getItemTemplate().getTemplateId());
            } else {
                writeD(buf, 0);
                writeD(buf, 0);
            }
            writeQ(buf, letter.getAttachedKinah());
            writeC(buf, 0);
        }
    }
