    public void writeObject(IDataWriter writer, ILocalMessageHeader object) throws IOException {
        int id = object.getId();
        int senderId = object.getSenderId();
        int recipientId = object.getRecipientId();
        String subject = object.getSubject();
        int threadId = object.getThreadId();
        long timestamp = object.getTimestamp();
        LocalMessageStatus status = object.getStatus();
        LocalMessageType type = object.getType();
        writer.writeInt(id);
        writer.writeInt(senderId);
        writer.writeInt(recipientId);
        writer.writeString(subject, true);
        writer.writeInt(threadId);
        writer.writeLong(timestamp);
        writer.writeEnum(status, true);
        writer.writeEnum(type, true);
    }
