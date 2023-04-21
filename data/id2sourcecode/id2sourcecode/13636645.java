    public ParsedAudioStream(URL url, AudioFormat audioFormat) {
        m_url = url;
        Debug.out("url: " + url);
        InputStream inputStream = null;
        try {
            inputStream = m_url.openStream();
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(inputStream);
            m_format = audioFormat;
            AudioFormat originalAudioFormat = audioStream.getFormat();
            if (!audioFormat.matches(originalAudioFormat)) {
                Debug.out("ParsedAudioStream.<init>(): audio formats do not match, trying to convert.");
                Debug.out("ParsedAudioStream.<init>(): source format: " + originalAudioFormat);
                Debug.out("ParsedAudioStream.<init>(): target format: " + audioFormat);
                AudioInputStream asold = audioStream;
                audioStream = AudioSystem.getAudioInputStream(getFormat(), asold);
                if (audioStream == null) {
                    Debug.out("ParsedAudioStream.<init>(): could not convert!");
                    Debug.out("ParsedAudioStream.<init>(): URL: " + url);
                    return;
                }
            }
            ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            while (true) {
                int bytesRead = audioStream.read(buffer);
                if (bytesRead == -1) {
                    break;
                }
                bAOS.write(buffer, 0, bytesRead);
            }
            m_data = bAOS.toByteArray();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
