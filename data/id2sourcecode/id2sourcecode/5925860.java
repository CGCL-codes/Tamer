    @Override
    public void playOnce() {
        if (line != null) {
            try {
                line.open(decodedFormat);
                byte[] data = new byte[4096];
                line.start();
                int nBytesRead;
                while (!stop && (nBytesRead = audioInputStream.read(data, 0, data.length)) != -1) line.write(data, 0, nBytesRead);
            } catch (IOException e) {
                stopPlaying();
                System.out.println("WARNING - could not open \"" + filename + "\" - sound will be disabled");
            } catch (LineUnavailableException e) {
                stopPlaying();
                System.out.println("WARNING - audio device is unavailable to play \"" + filename + "\" - sound will be disabled");
            }
        } else stopPlaying();
    }
