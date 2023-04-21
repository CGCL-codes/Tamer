    public void open(File f) throws AudioException {
        boolean supports = false;
        try {
            supports = AClipStorage.supports(f);
        } catch (IOException e) {
            throw new AudioException("ioError");
        } catch (Exception e) {
            Debug.printStackTrace(5, e);
            throw new AudioException("unexpectedError");
        }
        if (supports) {
            try {
                AClipStorage.load(clip, f);
            } catch (IOException e) {
                throw new AudioException("ioError");
            } catch (Exception e) {
                Debug.printStackTrace(5, e);
                throw new AudioException("unexpectedError");
            }
        } else {
            ALoad load = ALoadFactory.create(f);
            ALayer l = new ALayer(load.getChannels(), load.getSampleLength());
            try {
                setFileType(AudioSystem.getAudioFileFormat(f).getType());
                setEncoding(AudioSystem.getAudioInputStream(f).getFormat().getEncoding());
                Debug.println(3, "open clip " + f.getName() + ", " + l.getNumberOfChannels() + " channels, " + l.getMaxSampleLength() + " samples, " + AudioSystem.getAudioInputStream(f).getFormat().getEncoding().toString() + " encoding, " + AudioSystem.getAudioInputStream(f).getFormat().getSampleSizeInBits() + " bits, big-endian " + AudioSystem.getAudioInputStream(f).getFormat().isBigEndian());
            } catch (UnsupportedAudioFileException uafe) {
                Debug.printStackTrace(5, uafe);
                throw new AudioException("unsupportedAudioFormat");
            } catch (IOException ioe) {
                Debug.printStackTrace(5, ioe);
                throw new AudioException("ioError");
            } catch (Exception e) {
                Debug.printStackTrace(5, e);
                throw new AudioException("unexpectedError");
            }
            int s = 0;
            int d;
            int i = 0;
            try {
                LProgressViewer.getInstance().entrySubProgress(0.9, "loadSamples");
                while ((d = load.read(l, s, 4000)) >= 0) {
                    s += d;
                    Debug.println(3, "load sample " + s);
                    if (i++ % 256 == 0) {
                        LProgressViewer.getInstance().setNote(GLanguage.translate("load") + " " + (s / (int) load.getSampleRate()) + " " + GLanguage.translate("seconds") + "...");
                        if (load.getSampleLength() > 1) {
                            if (LProgressViewer.getInstance().setProgress(s / load.getSampleLength())) return;
                        } else {
                            if (LProgressViewer.getInstance().setUnknownProgress()) return;
                        }
                    }
                    Thread.yield();
                }
                LProgressViewer.getInstance().exitSubProgress();
            } catch (IOException ioe) {
                Debug.printStackTrace(5, ioe);
                throw new AudioException("ioError");
            } catch (Exception e) {
                Debug.printStackTrace(5, e);
                throw new AudioException("unexpectedError");
            }
            load.close();
            clip.add(l);
            clip.setSampleRate(load.getSampleRate());
            clip.setSampleWidth(load.getSampleWidth());
            clip.getPlotter().autoScale();
            setLoopEndPointer(clip.getMaxSampleLength());
        }
        file = f;
        clip.setName(f.getName());
    }
