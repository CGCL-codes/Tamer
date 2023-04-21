    private void writeXml(Writer writer) throws IOException {
        writer.write("<?xml version=\"1.0\" ?>\n");
        writer.write("<Session\n");
        writer.write("  beatsPerMinute=\"" + getBeatsPerMinute() + "\"\n");
        writer.write("  sessionName=\"" + getSessionName() + "\"\n");
        AudioFormat format = getAudioFormat();
        writer.write("<AudioFormat\n");
        writer.write("  sampleRate=\"" + format.getSampleRate() + "\"\n");
        writer.write("  sampleSizeInBits=\"" + format.getSampleSizeInBits() + "\"\n");
        writer.write("  channels=\"" + format.getChannels() + "\"\n");
        writer.write("  signed=\"" + (format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED) + "\"\n");
        writer.write("  bigEndian=\"" + format.isBigEndian() + "\" />\n");
        writer.write("<Tracks>\n");
        for (int nTrack = 0; nTrack < getTrackCount(); nTrack++) {
            getTrack(nTrack).writeXml(writer);
        }
        writer.write("</Tracks>\n");
        writer.write("<Phases>\n");
        for (int nPhase = 0; nPhase < getPhaseCount(); nPhase++) {
            getPhase(nPhase).writeXml(writer);
        }
        writer.write("</Phases>\n");
        writer.write("<Cells>\n");
        for (int nTrack = 0; nTrack < getTrackCount(); nTrack++) {
            for (int nPhase = 0; nPhase < getPhaseCount(); nPhase++) {
                getCell(nTrack, nPhase).writeXml(writer);
            }
        }
        writer.write("</Cells>\n");
        writer.write("</Session>\n");
    }
