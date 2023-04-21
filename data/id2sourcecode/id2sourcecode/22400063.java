    public AAudio(URL url) throws IOException {
        if (url == null) throw new IllegalArgumentException("null url");
        _name = getName(url);
        _isdata = url.openStream();
        _data = null;
        String format = null;
        try {
            format = AudioSystem.getAudioFileFormat(url).getType().getExtension();
        } catch (UnsupportedAudioFileException ex) {
            format = getFormatByName(_name);
            if (format == null) throw (IOException) new IOException().initCause(ex);
        }
        _format = format;
        _ctype = getContentType(_format);
    }
