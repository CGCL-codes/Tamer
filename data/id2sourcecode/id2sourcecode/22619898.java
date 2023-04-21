    private int convertFormat(AudioData ad) {
        switch(ad.getBitsPerSample()) {
            case 8:
                if (ad.getChannels() == 1) return AL_FORMAT_MONO8; else if (ad.getChannels() == 2) return AL_FORMAT_STEREO8;
                break;
            case 16:
                if (ad.getChannels() == 1) return AL_FORMAT_MONO16; else return AL_FORMAT_STEREO16;
        }
        throw new UnsupportedOperationException("Unsupported channels/bits combination: " + "bits=" + ad.getBitsPerSample() + ", channels=" + ad.getChannels());
    }
