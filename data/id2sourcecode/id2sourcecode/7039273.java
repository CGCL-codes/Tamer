    public boolean setDataSource(Object source) throws Exception {
        close();
        InputStream is = null;
        javax.sound.sampled.AudioFileFormat m_audioFileFormat = null;
        if (source instanceof ExtendedByteArrayInputStream) {
            is = checkInputStream((InputStream) source, ((ExtendedByteArrayInputStream) source).getName());
        } else if (source instanceof InputStream) {
            is = checkInputStream((InputStream) source, null);
        } else if (source instanceof File) {
            is = checkInputStream(new java.io.FileInputStream((File) source), ((File) source).getName());
        } else {
            java.net.URL url = null;
            if (source instanceof java.net.URL) url = (java.net.URL) source; else if (source instanceof String) {
                url = new java.net.URL((String) source);
            }
            if (url != null) {
                is = checkInputStream(url.openStream(), source.toString());
            }
        }
        if (is != null) {
            if (isMpeg || (!isWav && !isOgg)) {
                try {
                    m_audioFileFormat = AppletMpegSPIWorkaround.getAudioFileFormat(is);
                    ais = AppletMpegSPIWorkaround.getAudioInputStream(is);
                    isMpeg = true;
                } catch (IOException ex) {
                    throw ex;
                } catch (UnsupportedAudioFileException ex) {
                    isMpeg = false;
                }
            }
            if (isOgg || (!isMpeg && !isWav)) {
                try {
                    m_audioFileFormat = AppletVorbisSPIWorkaround.getAudioFileFormat(is);
                    ais = AppletVorbisSPIWorkaround.getAudioInputStream(is);
                    isOgg = true;
                } catch (IOException ex) {
                    throw ex;
                } catch (UnsupportedAudioFileException ex) {
                    isOgg = false;
                }
            }
            if (isWav || (!isMpeg && !isOgg)) {
                m_audioFileFormat = AudioSystem.getAudioFileFormat(is);
                ais = AudioSystem.getAudioInputStream(is);
            }
        }
        if (ais != null) {
            AudioFormat af = ais.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, af, INTERNAL_BUFFER_SIZE);
            if (!AudioSystem.isLineSupported(info)) {
                AudioFormat sourceFormat = af;
                AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), BIT_SAMPLE_SIZE, sourceFormat.getChannels(), sourceFormat.getChannels() * (BIT_SAMPLE_SIZE / 8), sourceFormat.getSampleRate(), BIG_ENDIAN);
                if (isMpeg) ais = AppletMpegSPIWorkaround.getAudioInputStream(targetFormat, ais); else if (isOgg) ais = AppletVorbisSPIWorkaround.getAudioInputStream(targetFormat, ais); else ais = AudioSystem.getAudioInputStream(targetFormat, ais);
            }
        }
        return ais != null;
    }
