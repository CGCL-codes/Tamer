    private boolean _convert(MediaLocator inML, MediaLocator outML) {
        try {
            p = Manager.createProcessor(inML);
        } catch (Exception e) {
            e.printStackTrace();
            fireConverterEvent(IConverterListener.CONVERTER_FAILURE, IConverterListener.CONVERTER_FAILURE_PROCESSOR_CREATION);
            return false;
        }
        p.addControllerListener(this);
        p.configure();
        if (!waitForState(p, Processor.Configured)) {
            fireConverterEvent(IConverterListener.CONVERTER_FAILURE, IConverterListener.CONVERTER_FAILURE_CONFIGURATION_TIMEOUT);
            return false;
        }
        setContentDescriptor(p, outML);
        Format[] fmts = new Format[1];
        AudioFileFormat inFormat = null;
        try {
            File f = new File(inML.getURL().getPath());
            inFormat = AudioSystem.getAudioFileFormat(f);
        } catch (Exception e) {
            fireConverterEvent(IConverterListener.CONVERTER_FAILURE, IConverterListener.CONVERTER_FAILURE_TRACK_FORMAT);
            return false;
        }
        fmts[0] = new AudioFormat(AudioFormat.MPEG, Math.max(inFormat.getFormat().getSampleRate(), 22050.0), inFormat.getFormat().getFrameSize() * 8 / inFormat.getFormat().getChannels(), 2);
        if (!setTrackFormats(p, fmts)) {
            fireConverterEvent(IConverterListener.CONVERTER_FAILURE, IConverterListener.CONVERTER_FAILURE_TRACK_FORMAT);
            return false;
        }
        p.realize();
        if (!waitForState(p, Processor.Realized)) {
            fireConverterEvent(IConverterListener.CONVERTER_FAILURE, IConverterListener.CONVERTER_FAILURE_NOT_REALIZED);
            return false;
        }
        if ((dsink = createDataSink(p, outML)) == null) {
            fireConverterEvent(IConverterListener.CONVERTER_FAILURE, IConverterListener.CONVERTER_FAILURE_OUTPUT_FILE_CREATION);
            return false;
        }
        dsink.addDataSinkListener(this);
        fileDone = false;
        try {
            p.start();
            dsink.start();
        } catch (IOException e) {
            fireConverterEvent(IConverterListener.CONVERTER_FAILURE, IConverterListener.CONVERTER_FAILURE_IO_ERROR);
            return false;
        }
        return true;
    }
