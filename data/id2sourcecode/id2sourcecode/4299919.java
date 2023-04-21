    private CorporaEntry store(InputStream inputStream, AudioFileFormat format) {
        CorporaEntry entry = new CorporaEntry();
        String fileName = createName(entry.getObjectId());
        GridFSInputFile audioForInput = createFile(inputStream, fileName);
        audioForInput.save();
        if (format != null) {
            float lengthInSec = format.getFrameLength() / format.getFormat().getFrameRate();
            float sampleRate = format.getFormat().getSampleRate();
            int channels = format.getFormat().getChannels();
            int sampleSizeInBits = format.getFormat().getSampleSizeInBits();
            entry.setLengthInSec(lengthInSec);
            entry.setSampleRate(sampleRate);
            entry.setChannels(channels);
            entry.setSampleSizeInBits(sampleSizeInBits);
        }
        entry.setFileName(fileName);
        entry.setTimeStamp(System.currentTimeMillis());
        entry.setCreated(Calendar.getInstance().getTime());
        entry.setFileSize(audioForInput.getLength());
        mongoOperations.insert(entry);
        return entry;
    }
