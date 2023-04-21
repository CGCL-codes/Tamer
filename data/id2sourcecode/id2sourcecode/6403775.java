    public AudioFormat abrirAudioStream(SonidoReproduciendo sonidoRep) {
        if (mixer == null) {
            return null;
        }
        File fichero = new File(sonidoRep.sonido.path);
        try {
            sonidoRep.audioInputStreamCodificada = AudioSystem.getAudioInputStream(fichero);
        } catch (UnsupportedAudioFileException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        if (sonidoRep.audioInputStreamCodificada == null) {
            return null;
        }
        AudioFormat formatoOriginal = sonidoRep.audioInputStreamCodificada.getFormat();
        AudioFormat formatoDecodificado = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, formatoOriginal.getSampleRate(), 16, formatoOriginal.getChannels(), formatoOriginal.getChannels() * 2, formatoOriginal.getSampleRate(), false);
        sonidoRep.audioInputStreamDecodificada = AudioSystem.getAudioInputStream(formatoDecodificado, sonidoRep.audioInputStreamCodificada);
        return formatoDecodificado;
    }
