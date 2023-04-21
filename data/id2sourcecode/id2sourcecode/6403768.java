    private int nuevoSonidoInterno(String path, int tipo, boolean estereo, int datosSample[], int samplesPorSegundo) {
        if (path == null || path.equals("")) {
            return -1;
        }
        if (tipo < 0 || tipo > MAX_TIPOS_SAMPLE) {
            return -1;
        }
        if (tipo != TIPO_SAMPLE_SINTETIZADO) {
            File fichero = new File(path);
            if (!fichero.exists() || !fichero.isFile()) {
                return -1;
            }
        }
        int indiceSonido = buscarSonido(path);
        if (indiceSonido != -1) {
            return indiceSonido;
        }
        if (sonidos.size() == 0) {
            if ((tipo == TIPO_SAMPLE_SINTETIZADO || (tipo == TIPO_SAMPLE_EN_MEMORIA && datosSample != null)) && samplesPorSegundo != 44100 && samplesPorSegundo != 22050 && samplesPorSegundo != 11025) {
                return -1;
            }
        }
        Sonido sonido = new Sonido();
        sonido.tipo = tipo;
        sonido.path = path;
        AudioFormat audioFormat = null;
        switch(tipo) {
            case TIPO_SAMPLE_SINTETIZADO:
                sonido.estereo = estereo;
                if (sonidos.size() == 0) {
                    audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, samplesPorSegundo, 16, 2, 2 * 2, samplesPorSegundo, false);
                }
                break;
            case TIPO_SAMPLE_EN_MEMORIA:
                if (datosSample != null) {
                    sonido.buferSample = datosSample;
                    sonido.estereo = estereo;
                    sonido.numSamples = sonido.buferSample.length / (sonido.estereo ? 2 : 1);
                    if (sonidos.size() == 0) {
                        audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, samplesPorSegundo, 16, 2, 2 * 2, samplesPorSegundo, false);
                    }
                } else {
                    sonidoRepCarga.sonido = sonido;
                    audioFormat = abrirAudioStream(sonidoRepCarga);
                    if (audioFormat == null) {
                        return -1;
                    }
                    if (sonidos.size() > 0 && audioFormat.getSampleRate() != this.samplesPorSegundo) {
                        Log.log("Cannot load sound file, sample rate is not the same of the system or number of channels is not 2. File name: [" + sonido.path + "]");
                        return -1;
                    }
                    sonido.estereo = audioFormat.getChannels() == 2;
                    sonido.numSamples = (int) sonidoRepCarga.audioInputStreamDecodificada.getFrameLength();
                    if (sonido.numSamples == -1) {
                        return -1;
                    }
                    if (sonido.estereo) {
                        sonido.buferSample = new int[sonido.numSamples * 2];
                    } else {
                        sonido.buferSample = new int[sonido.numSamples];
                    }
                    try {
                        try {
                            int nBytesLeidos = 0;
                            int numSampleCh = 0;
                            while (nBytesLeidos != -1) {
                                nBytesLeidos = sonidoRepCarga.audioInputStreamDecodificada.read(buferCarga, 0, buferCarga.length);
                                if (nBytesLeidos > 0) {
                                    int posCarga = 0;
                                    for (posCarga = 0; posCarga < nBytesLeidos; posCarga += 2) {
                                        int byteBajo = buferCarga[posCarga];
                                        int byteAlto = buferCarga[posCarga + 1];
                                        int valor = (byteBajo & 0xFF) | ((byteAlto << 8) & 0xFF00);
                                        sonido.buferSample[numSampleCh++] = valor;
                                    }
                                    if (posCarga != nBytesLeidos) {
                                        int kk = 34;
                                        int gg = kk;
                                    }
                                }
                            }
                        } catch (IOException e) {
                            return -1;
                        }
                    } finally {
                        cerrarAudioStream(sonidoRepCarga);
                        sonidoRepCarga.sonido = null;
                    }
                }
                break;
            case TIPO_SAMPLE_STREAMING:
                break;
            default:
                return -1;
        }
        if (sonidos.size() == 0) {
            AudioFormat audioFormat2 = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 2 * 2, 44100, false);
            if (audioFormat == null || !abrirDispositivoSonido(audioFormat2)) {
                return -1;
            }
        }
        sonidos.add(sonido);
        indiceSonido = sonidos.size() - 1;
        sonido.indiceSonido = indiceSonido;
        return indiceSonido;
    }
