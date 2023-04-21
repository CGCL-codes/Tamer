    public boolean stream(int bufferId) {
        try {
            int count = audio.read(buffer);
            if (count != -1) {
                bufferData.clear();
                bufferData.put(buffer, 0, count);
                bufferData.flip();
                int format = audio.getChannels() > 1 ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16;
                try {
                    AL10.alBufferData(bufferId, format, bufferData, audio.getRate());
                } catch (OpenALException e) {
                    e.printStackTrace();
                    Log.error("Failed to loop buffer: " + bufferId + " " + format + " " + count + " " + audio.getRate());
                    return false;
                }
            } else {
                if (loop) {
                    initStreams();
                    stream(bufferId);
                } else {
                    done = true;
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            Log.error(e);
            return false;
        }
    }
