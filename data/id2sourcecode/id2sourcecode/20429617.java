    public void commandGETRECORDINGSFROM(Long from) {
        SortedSet<RecordingData> datas = controller.getRecordingsFrom(from);
        for (RecordingData data : datas) {
            System.out.println("(" + data.getId() + ")\t" + data.getChannel() + "\t" + toTimeString(data.getStart()) + "\t" + toTimeString(data.getEnd()) + "\t" + data.getName());
        }
    }
