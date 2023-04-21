    public AudioFloatInputStream openStream() {
        if (buffer == null) return null;
        if (format == null) {
            InputStream is = buffer.getInputStream();
            AudioInputStream ais = null;
            try {
                ais = AudioSystem.getAudioInputStream(is);
            } catch (Exception e) {
                return null;
            }
            return AudioFloatInputStream.getInputStream(ais);
        }
        if (buffer.array() == null) {
            return AudioFloatInputStream.getInputStream(new AudioInputStream(buffer.getInputStream(), format, buffer.capacity()));
        }
        if (buffer8 != null) {
            if (format.getEncoding().equals(Encoding.PCM_SIGNED) || format.getEncoding().equals(Encoding.PCM_UNSIGNED)) {
                InputStream is = new Buffer8PlusInputStream();
                AudioFormat format2 = new AudioFormat(format.getEncoding(), format.getSampleRate(), format.getSampleSizeInBits() + 8, format.getChannels(), format.getFrameSize() + (1 * format.getChannels()), format.getFrameRate(), format.isBigEndian());
                AudioInputStream ais = new AudioInputStream(is, format2, buffer.capacity() / format.getFrameSize());
                return AudioFloatInputStream.getInputStream(ais);
            }
        }
        return AudioFloatInputStream.getInputStream(format, buffer.array(), (int) buffer.arrayOffset(), (int) buffer.capacity());
    }
