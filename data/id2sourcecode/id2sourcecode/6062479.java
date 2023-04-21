    protected boolean initDevice(AudioFormat format) {
        if (format == null) {
            System.err.println("AudioRenderer: ERROR: Unknown AudioFormat");
            return false;
        }
        if (format.getSampleRate() == Format.NOT_SPECIFIED || format.getSampleSizeInBits() == Format.NOT_SPECIFIED) {
            Log.error("Cannot initialize audio renderer with format: " + format);
            return false;
        }
        if (device != null) {
            device.drain();
            pauseDevice();
            mediaTimeAnchor = getMediaNanoseconds();
            ticksSinceLastReset = 0;
            device.dispose();
            device = null;
        }
        AudioFormat audioFormat = new AudioFormat(format.getEncoding(), format.getSampleRate(), format.getSampleSizeInBits(), format.getChannels(), format.getEndian(), format.getSigned());
        device = createDevice(audioFormat);
        if (device == null || !device.initialize(audioFormat, computeBufferSize(audioFormat))) {
            device = null;
            return false;
        }
        device.setMute(gainControl.getMute());
        device.setGain(gainControl.getDB());
        if (rate != 1.0f) {
            if (rate != device.setRate(rate)) {
                System.err.println("The AudioRenderer does not support the given rate: " + rate);
                device.setRate(1.0f);
            }
        }
        if (started) resumeDevice();
        bytesPerSec = (int) (format.getSampleRate() * format.getChannels() * format.getSampleSizeInBits() / 8);
        return true;
    }
