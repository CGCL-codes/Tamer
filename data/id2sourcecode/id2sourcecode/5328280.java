    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("[doPost] request: {0}", getConfigService().reconstructRequestURLandParams(request.getRequestURL().toString(), request.getParameterMap()));
        LOG.debug("[doPost] Handling portal recognize() post on session: " + request.getSession().getId());
        AudioFormat audioFormat = getAudioFormatFromParams(request, "recordAudioFormat", "recordSampleRate", "recordIsLittleEndian");
        LOG.debug("[doPost]  audioFormat={0}", audioFormat);
        AudioInputStream audioIn = new AudioInputStream(new BufferedInputStream(request.getInputStream()), audioFormat, AudioSystem.NOT_SPECIFIED);
        AudioFormat requiredFormat = getRecognizerRequiredAudioFormat();
        if (audioFormat.getEncoding() != requiredFormat.getEncoding() || audioFormat.getSampleRate() != requiredFormat.getSampleRate() || audioFormat.getSampleSizeInBits() != requiredFormat.getSampleSizeInBits() || audioFormat.getChannels() != requiredFormat.getChannels() || audioFormat.getFrameSize() != requiredFormat.getFrameSize() || audioFormat.getFrameRate() != requiredFormat.getFrameRate() || audioFormat.isBigEndian() != requiredFormat.isBigEndian()) {
            LOG.debug("[doPost] Resampling");
            audioIn = new WamiResampleAudioInputStream(getRecognizerRequiredAudioFormat(), audioIn);
        }
        getStorageService().store(audioIn);
    }
