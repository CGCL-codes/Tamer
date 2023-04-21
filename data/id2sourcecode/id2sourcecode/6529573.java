    protected boolean loadBuffer() {
        if (!_initialised) {
            return false;
        }
        init();
        byte[] buffer;
        File file = new File(FileUtil.getExternalFilename(this.getURL()));
        try {
            _audioInputStream = AudioSystem.getAudioInputStream(file);
        } catch (UnsupportedAudioFileException ex) {
            log.error("Unsupported audio file format when loading buffer:" + ex);
            return false;
        } catch (IOException ex) {
            log.error("Error loading buffer:" + ex);
            return false;
        }
        _audioFormat = _audioInputStream.getFormat();
        this._freq = (int) _audioFormat.getSampleRate();
        int dataSize = _audioFormat.getChannels() * (int) _audioInputStream.getFrameLength() * _audioFormat.getSampleSizeInBits() / 8;
        if (log.isDebugEnabled()) log.debug("Size of JavaSoundAudioBuffer (" + this.getSystemName() + ") = " + dataSize);
        if (dataSize > 0) {
            buffer = new byte[dataSize];
            int bytesRead = 0, totalBytesRead = 0;
            try {
                log.debug("Start to load JavaSoundBuffer...");
                while ((bytesRead = _audioInputStream.read(buffer, totalBytesRead, buffer.length - totalBytesRead)) != -1 && totalBytesRead < buffer.length) {
                    log.debug("read " + bytesRead + " bytes of total " + dataSize);
                    totalBytesRead += bytesRead;
                }
            } catch (IOException ex) {
                log.error("Error when reading JavaSoundAudioBuffer (" + this.getSystemName() + ") " + ex);
                return false;
            }
            log.debug("...finished loading JavaSoundBuffer");
        } else {
            log.warn("Unable to determine length of JavaSoundAudioBuffer (" + this.getSystemName() + ")");
            log.warn(" - buffer has not been loaded.");
            return false;
        }
        this._dataStorageBuffer = convertAudioEndianness(buffer, _audioFormat.getSampleSizeInBits() == 16);
        this.setStartLoopPoint(0, false);
        this.setEndLoopPoint(_audioInputStream.getFrameLength(), false);
        this.generateLoopBuffers(LOOP_POINT_BOTH);
        this.setState(STATE_LOADED);
        if (log.isDebugEnabled()) {
            log.debug("Loaded buffer: " + this.getSystemName());
            log.debug(" from file: " + this.getURL());
            log.debug(" format: " + parseFormat() + ", " + _freq + " Hz");
            log.debug(" length: " + _audioInputStream.getFrameLength());
        }
        return true;
    }
