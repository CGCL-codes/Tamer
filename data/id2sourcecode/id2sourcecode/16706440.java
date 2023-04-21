    public Object getValueAt(int nRow, int nColumn) {
        AudioFormat format = getAudioFormats()[nRow];
        switch(nColumn) {
            case 0:
                return format.getEncoding().toString();
            case 1:
                float fSampleRate = format.getSampleRate();
                return (fSampleRate == AudioSystem.NOT_SPECIFIED) ? "any" : Float.toString(fSampleRate);
            case 2:
                int nSampleSize = format.getSampleSizeInBits();
                return (nSampleSize == AudioSystem.NOT_SPECIFIED) ? "any" : Integer.toString(nSampleSize);
            case 3:
                int nChannels = format.getChannels();
                return (nChannels == AudioSystem.NOT_SPECIFIED) ? "any" : Integer.toString(nChannels);
            case 4:
                int nFrameSize = format.getFrameSize();
                return (nFrameSize == AudioSystem.NOT_SPECIFIED) ? "any" : Integer.toString(nFrameSize);
            case 5:
                float fFrameRate = format.getFrameRate();
                return (fFrameRate == AudioSystem.NOT_SPECIFIED) ? "any" : Float.toString(fFrameRate);
            case 6:
                return format.isBigEndian() ? "big" : "little";
            default:
                return null;
        }
    }
