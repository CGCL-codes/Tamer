    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == load) {
            File f;
            while (true) {
                if (fc.showOpenDialog(LGM.frame) != JFileChooser.APPROVE_OPTION) return;
                f = fc.getSelectedFile();
                if (f.exists()) break;
                JOptionPane.showMessageDialog(null, f.getName() + Messages.getString("SoundFrame.FILE_MISSING"), Messages.getString("SoundFrame.FILE_OPEN"), JOptionPane.WARNING_MESSAGE);
            }
            try {
                data = fileToBytes(f);
                String fn = f.getName();
                res.put(PSound.FILE_NAME, fn);
                String ft = CustomFileFilter.getExtension(fn);
                if (ft == null) ft = "";
                res.put(PSound.FILE_TYPE, ft);
                filename.setText(Messages.format("SoundFrame.FILE", fn));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            modified = true;
            cleanup();
            return;
        }
        if (e.getSource() == play) {
            if (data == null || data.length == 0) return;
            try {
                InputStream source = new ByteArrayInputStream(data);
                AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(source));
                AudioFormat fmt = ais.getFormat();
                if (fmt.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                    fmt = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, fmt.getSampleRate(), fmt.getSampleSizeInBits() * 2, fmt.getChannels(), fmt.getFrameSize() * 2, fmt.getFrameRate(), true);
                    ais = AudioSystem.getAudioInputStream(fmt, ais);
                }
                final Clip clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, fmt));
                clip.open(ais);
                new Thread() {

                    public void run() {
                        clip.start();
                        try {
                            do Thread.sleep(99); while (clip.isActive());
                        } catch (InterruptedException e) {
                        }
                        clip.stop();
                        clip.close();
                    }
                }.start();
            } catch (UnsupportedAudioFileException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (LineUnavailableException e1) {
                e1.printStackTrace();
            }
            return;
        }
        if (e.getSource() == stop) {
            if (clip != null) clip.stop();
            return;
        }
        if (e.getSource() == store) {
            if (fc.showSaveDialog(LGM.frame) != JFileChooser.APPROVE_OPTION) return;
            try {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fc.getSelectedFile()));
                out.write(data);
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return;
        }
        if (e.getSource() == edit) {
            try {
                if (editor == null) new SoundEditor(); else editor.start();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return;
        }
        if (e.getSource() == center) {
            pan.setValue(0);
            return;
        }
        super.actionPerformed(e);
    }
