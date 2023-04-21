    public AuInput(AudioInputStream audioInputStream, int Numar) {
        NumarCanalCitit = Numar;
        sampleSizeInBits = audioInputStream.getFormat().getSampleSizeInBits();
        channels = audioInputStream.getFormat().getChannels();
        frecventa = (int) audioInputStream.getFormat().getSampleRate();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] audioBytes = new byte[audioInputStream.getFormat().getFrameSize()];
        try {
            while ((audioInputStream.read(audioBytes)) != -1) {
                output.write(audioBytes);
            }
        } catch (Exception e) {
            System.out.println("Eroare " + e);
        }
        try {
            audioInputStream.close();
        } catch (IOException e) {
            System.out.println("Eroare la inchiderea fisierului");
        }
        data = new DataInputStream(new ByteArrayInputStream(output.toByteArray()));
    }
