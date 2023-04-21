    private void criar() throws AudioException {
        byte[] buffer;
        int numBytesLidos;
        File arqSomOriginal;
        FileOutputStream audioSaida;
        AudioFormat formatoOriginal;
        AudioInputStream audioOriginal;
        AudioInputStream audioConvertido;
        arqSomOriginal = new File(nomeArquivo);
        nomeArquivoTemp = "/tmp/audio" + arqSomOriginal.getName();
        try {
            audioOriginal = AudioSystem.getAudioInputStream(arqSomOriginal);
        } catch (Exception e) {
            estado = PARADO;
            throw new AudioException(e.getMessage());
        }
        formatoOriginal = audioOriginal.getFormat();
        bigEndian = false;
        sampleSizeBytes = 2;
        channels = formatoOriginal.getChannels();
        frameSize = formatoOriginal.getChannels() * sampleSizeBytes;
        frameRate = formatoOriginal.getSampleRate();
        sampleRate = formatoOriginal.getSampleRate();
        tamBloco = kByte * channels;
        buffer = new byte[tamBloco];
        formatoNovo = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, sampleSizeBytes * 8, channels, frameSize, frameRate, bigEndian);
        try {
            estado = NORMALIZAR;
            audioConvertido = AudioSystem.getAudioInputStream(formatoNovo, audioOriginal);
            audioSaida = new FileOutputStream(new File(nomeArquivoTemp));
            tamArqBytes = 0;
            tamArqBlocos = 0;
            while ((numBytesLidos = audioConvertido.read(buffer, 0, buffer.length)) != -1) {
                if (estado != NORMALIZAR) {
                    audioConvertido.close();
                    audioOriginal.close();
                    audioSaida.close();
                    return;
                }
                tamArqBlocos++;
                tamArqBytes += numBytesLidos;
                audioSaida.write(buffer, 0, numBytesLidos);
            }
            samplesPorCanal = tamArqBytes / frameSize;
            audioConvertido.close();
            audioOriginal.close();
            audioSaida.close();
            estado = INICIAR;
            System.out.println("Fim da Normalizacao - Estado Iniciar... ");
        } catch (IOException e) {
            estado = PARADO;
            throw new AudioException(e.getMessage());
        }
        System.out.println("Lidos = " + tamArqBlocos + " blocos");
    }
