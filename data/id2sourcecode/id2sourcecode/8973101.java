    public void removeMessage(Object uid) throws Exception {
        MboxMessage message = (MboxMessage) messages.remove(uid);
        if (uid.equals(largestUid)) {
            FileChannel channel = new RandomAccessFile(mboxFile, "rw").getChannel();
            channel.truncate(mboxFile.length() - (message.getLength() + FROMLINE.length() + TERMLINE.length));
            channel.close();
        } else {
            int intUid = ((Integer) uid).intValue();
            deleteFilePart(mboxFile, message.getStart() - FROMLINE.length(), message.getLength() + FROMLINE.length() + TERMLINE.length);
            Enumeration uids = messages.keys();
            while (uids.hasMoreElements()) {
                Integer actUid = (Integer) uids.nextElement();
                if (actUid.intValue() > intUid) {
                    MboxMessage m = (MboxMessage) messages.get(actUid);
                    m.setStart(m.getStart() - (message.getLength() + FROMLINE.length() + TERMLINE.length));
                }
            }
        }
    }
