    public int getChannel() {
        libvlc_exception_t exception = new libvlc_exception_t();
        return jvlc.getLibvlc().libvlc_audio_get_channel(jvlc.getInstance(), exception);
    }
