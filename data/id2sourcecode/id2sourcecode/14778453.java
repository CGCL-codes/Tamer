    @Override
    public void writeImpl(AionConnection con, ByteBuffer buf) {
        switch(serviceId) {
            case 0:
                writeMailboxState(buf, mailCount, unreadCount, hasExpress);
                break;
            case 1:
                writeMailMessage(buf, mailMessage);
                break;
            case 2:
                if (letters.size() > 0) writeLettersList(buf, letters, player); else writeEmptyLettersList(buf, player);
                break;
            case 3:
                writeLetterRead(buf, player, letter, time);
                break;
            case 5:
                writeLetterState(buf, letterId, attachmentType);
                break;
            case 6:
                writeLetterDelete(buf, player, letterId);
                break;
        }
    }
