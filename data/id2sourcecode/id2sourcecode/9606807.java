    public void open(AudioFormat format, byte[] data, int offset, int bufferSize) throws LineUnavailableException {
        synchronized (control_mutex) {
            if (isOpen()) {
                throw new IllegalStateException("Clip is already open with format " + getFormat() + " and frame lengh of " + getFrameLength());
            }
            if (AudioFloatConverter.getConverter(format) == null) throw new IllegalArgumentException("Invalid format : " + format.toString());
            if (bufferSize % format.getFrameSize() != 0) throw new IllegalArgumentException("Buffer size does not represent an integral number of sample frames!");
            this.data = data;
            this.offset = offset;
            this.bufferSize = bufferSize;
            this.format = format;
            this.framesize = format.getFrameSize();
            loopstart = 0;
            loopend = -1;
            loop_sg = true;
            if (!mixer.isOpen()) {
                mixer.open();
                mixer.implicitOpen = true;
            }
            outputformat = mixer.getFormat();
            out_nrofchannels = outputformat.getChannels();
            in_nrofchannels = format.getChannels();
            open = true;
            mixer.getMainMixer().openLine(this);
        }
    }
