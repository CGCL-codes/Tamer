    @Override
    protected void writeImpl() {
        writeC(0xfe);
        writeH(0xaa);
        writeD((int) (System.currentTimeMillis() / 1000));
        if (_inbox != null && _inbox.size() > 0) {
            writeD(_inbox.size());
            for (Message msg : _inbox) {
                writeD(msg.getId());
                writeS(msg.getSubject());
                writeS(msg.getSenderName());
                writeD(msg.isLocked() ? 0x01 : 0x00);
                writeD(msg.getExpirationSeconds());
                writeD(msg.isUnread() ? 0x01 : 0x00);
                writeD(0x01);
                writeD(msg.hasAttachments() ? 0x01 : 0x00);
                writeD(msg.isFourStars() ? 0x01 : 0x00);
                writeD(msg.isNews() ? 0x01 : 0x00);
            }
        } else {
            writeD(0x00);
        }
        FastList.recycle((FastList<Message>) _inbox);
        _inbox = null;
    }
