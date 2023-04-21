    JALSound(JALSoundManager factory, SoundInfo info) throws Exception {
        this.factory = factory;
        this.al = factory.al;
        this.info = info;
        JALSoundManager.checkError(al);
        this.bytes = info.getData();
        if (bytes != null) {
            this.size = bytes.remaining();
            int format = AL.AL_FORMAT_MONO16;
            if (info.getBitLength() == 16) {
                if (info.getChannels() == 1) {
                    format = AL.AL_FORMAT_MONO16;
                } else {
                    format = AL.AL_FORMAT_STEREO16;
                }
            } else if (info.getBitLength() == 8) {
                if (info.getChannels() == 1) {
                    format = AL.AL_FORMAT_MONO8;
                } else {
                    format = AL.AL_FORMAT_STEREO8;
                }
            }
            int[] buffer = new int[1];
            al.alGenBuffers(1, buffer, 0);
            if (JALSoundManager.checkError(al)) {
                throw new Exception("Error generating OpenAL buffers : " + toString());
            } else {
                this.buffer = buffer;
            }
            al.alBufferData(buffer[0], format, bytes, size, info.getFrameRate());
            if (JALSoundManager.checkError(al)) {
                al.alDeleteBuffers(1, buffer, 0);
                if (JALSoundManager.checkError(al)) {
                }
                this.buffer = null;
                throw new Exception("Error generating OpenAL buffers : " + toString());
            }
        }
    }
