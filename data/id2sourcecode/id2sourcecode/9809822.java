    private void play() {
        int offset, count;
        continuePlaying = true;
        openAudioFile(new File(nomefile));
        if (canvas.getSelectionStart() == 0 && canvas.getSelectionEnd() == 0) {
            offset = 0;
            count = (int) ais.getFrameLength() * af.getFrameSize();
        } else {
            offset = canvas.getSelectionStart();
            count = (canvas.getSelectionEnd() - canvas.getSelectionStart());
            System.out.println("Suona da " + offset + " a " + (offset + count) + " in tutto " + count + " frames, " + count / af.getFrameRate() + " secondi");
        }
        int ok = 0;
        int read = 0;
        offset = offset * af.getFrameSize();
        count = count * af.getFrameSize();
        byte[] buffer = new byte[count];
        try {
            ais.skip(offset);
            while (continuePlaying && (ok < count) && (read = ais.read(buffer)) != -1) {
                ok += read;
                int w = line.write(buffer, 0, read);
                System.out.println("Letti " + read + " bytes, scritti " + w + " bytes");
            }
            ais.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        continuePlaying = false;
    }
