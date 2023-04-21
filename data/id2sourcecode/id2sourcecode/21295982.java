    public org.jvoicexml.jsapi2.AudioFormat getAudioFormat() throws AudioException {
        final String locator = getMediaLocator();
        if (locator != null) {
            URL url = null;
            try {
                url = new URL(locator);
                AudioFormat format = JavaSoundParser.parse(url);
                return new org.jvoicexml.jsapi2.AudioFormat(format.getEncoding().toString(), format.getSampleRate(), format.getSampleSizeInBits(), format.getChannels(), format.getFrameSize(), format.getFrameRate(), format.isBigEndian());
            } catch (MalformedURLException ex) {
                throw new AudioException(ex.getMessage());
            } catch (URISyntaxException ex) {
                throw new AudioException(ex.getMessage());
            }
        }
        return new org.jvoicexml.jsapi2.AudioFormat(engineAudioFormat.getEncoding().toString(), engineAudioFormat.getSampleRate(), engineAudioFormat.getSampleSizeInBits(), engineAudioFormat.getChannels(), engineAudioFormat.getFrameSize(), engineAudioFormat.getFrameRate(), engineAudioFormat.isBigEndian());
    }
