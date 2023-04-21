    @Override
    public String getDurationAdditionalMsg() throws Exception {
        if (!Utils.isBlank(m_rawSoundFile)) {
            File rawSoundFile = new File(m_rawSoundFile);
            FileInputStream fin = null;
            try {
                fin = new FileInputStream(rawSoundFile);
            } catch (FileNotFoundException e) {
                Dialogs.showNoWayDialog("Unable to find raw sound file");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            FileChannel in = fin.getChannel();
            try {
                int chunkSize = RecordedSoundPeerImpl.readRiffChunk(in);
                FormatChunkInfo info = RecordedSoundPeerImpl.readFormatChunk(in);
                int dataChunkSize = RecordedSoundPeerImpl.readDataChunkHeader(in);
                StringBuffer msg = new StringBuffer();
                msg.append("Raw file: Sample rate = ");
                msg.append(info.m_sampleRate);
                msg.append(" Avg bytes/sec = ");
                msg.append(info.m_avgBytesPerSec);
                msg.append(" Bits/sample = ");
                msg.append(info.m_bitsPerSample);
                msg.append(" Bytes/frame = ");
                msg.append(info.m_blockAlign);
                msg.append(" Channels = ");
                msg.append(info.m_numChannels);
                msg.append("\n               Data chunk size = ");
                msg.append(dataChunkSize);
                int numSamples = dataChunkSize / info.m_blockAlign;
                msg.append(" Samples = ");
                msg.append(numSamples);
                double duration = (double) dataChunkSize / info.m_avgBytesPerSec;
                msg.append(" Duration = ");
                msg.append(duration);
                return msg.toString();
            } catch (RuntimeException e) {
                e.printStackTrace();
                throw e;
            } finally {
                in.close();
                fin.close();
            }
        }
        return null;
    }
