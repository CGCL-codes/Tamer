    public void readObject(DataInputStream in, int version) throws IOException {
        long dtAdded = in.readLong();
        dateAdded = dtAdded == -1 ? null : new Date(dtAdded);
        int statusOrdinal = in.readInt();
        if (statusOrdinal == -1) {
            status = null;
        } else {
            TaskStatus[] statusList = TaskStatus.values();
            status = statusList[statusOrdinal];
        }
        priority = in.readInt();
        int intervalOrdinal = in.readInt();
        if (intervalOrdinal == -1) {
            reminderInterval = null;
        } else {
            ReminderInterval[] intervals = ReminderInterval.values();
            reminderInterval = intervals[intervalOrdinal];
        }
        long dtReminderFrom = in.readLong();
        reminderDateFrom = dtReminderFrom == -1 ? null : dtReminderFrom;
        long dtLastNotification = in.readLong();
        lastNotification = dtLastNotification == -1 ? null : dtLastNotification;
        int nextRmLength = in.readInt();
        nextReminderLength = nextRmLength == -1 ? null : nextRmLength;
        long estTime = in.readLong();
        estimatedTime = estTime == -1 ? null : estTime;
        title = in.readUTF();
        comments = in.readUTF();
        User req = new User();
        req.readObject(in, 1);
        setRequestor(req);
        User act = new User();
        act.readObject(in, 1);
        setActioner(act);
        idMap.clear();
        int idCount = in.readInt();
        for (int i = 0; i < idCount; i++) {
            MachineID readMachineID = new MachineID();
            readMachineID.readObject(in, 1);
            long taskID = in.readLong();
            idMap.put(readMachineID, taskID);
        }
        File tmpFolder = File.createTempFile("attach_", ".cclearly");
        tmpFolder.delete();
        tmpFolder.mkdir();
        tmpFolder.deleteOnExit();
        int attachCount = in.readInt();
        newTempFiles = new File[attachCount];
        for (int i = 0; i < attachCount; i++) {
            String fileName = in.readUTF();
            File tmpFile = new File(tmpFolder, fileName);
            tmpFile.deleteOnExit();
            newTempFiles[i] = tmpFile;
            FileOutputStream out = new FileOutputStream(tmpFile, false);
            long fileSize = in.readLong();
            long fileRead = 0;
            byte[] readFile = new byte[4096];
            while (fileRead != fileSize) {
                int readAmount = (int) Math.min(4000, fileSize - fileRead);
                int realRead = in.read(readFile, 0, readAmount);
                out.write(readFile, 0, realRead);
                fileRead += realRead;
            }
            out.close();
        }
        if (version >= 2) {
            boolean hasTargetDate = in.readBoolean();
            if (hasTargetDate) {
                targetDate = new Date(in.readLong());
            }
        }
        if (version >= 3) {
            boolean hasDateArchived = in.readBoolean();
            if (hasDateArchived) {
                dateArchived = new Date(in.readLong());
            }
        }
        if (version >= 4) {
            boolean hasAttachmentMetadata = in.readBoolean();
            attachmentMetadata = new LinkedList<Attachment>();
            if (hasAttachmentMetadata) {
                int count = in.readInt();
                for (int i = 0; i < count; i++) {
                    Attachment attachment = new Attachment();
                    attachment.readObject(in, 2);
                    attachmentMetadata.add(attachment);
                }
            }
            boolean hasRequestorFolder = in.readBoolean();
            if (hasRequestorFolder) {
                requestorFolder = in.readUTF();
            }
            boolean hasNotes = in.readBoolean();
            if (hasNotes) {
                int noteCount = in.readInt();
                for (int i = 0; i < noteCount; i++) {
                    TaskNote note = new TaskNote();
                    note.readObject(in, 2);
                    addNote(note);
                }
            }
        }
        if (version >= 5) {
            boolean hasTaskNumber = in.readBoolean();
            if (hasTaskNumber) {
                taskNumber = in.readUTF();
            }
        }
        if (version >= 6) {
            boolean hasCapturedBy = in.readBoolean();
            if (hasCapturedBy) {
                capturedBy = new User();
                capturedBy.readObject(in, 1);
            }
        }
    }
