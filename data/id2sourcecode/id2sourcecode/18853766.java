    private void LoadWaveActionPerformed(java.awt.event.ActionEvent evt) {
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                AudioInputStream stream = AudioSystem.getAudioInputStream(file);
                AudioFormat format = stream.getFormat();
                if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                    format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                    stream = AudioSystem.getAudioInputStream(format, stream);
                }
                DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat(), ((int) stream.getFrameLength() * format.getFrameSize()));
                clip = (Clip) AudioSystem.getLine(info);
                clip.open(stream);
                clip.addLineListener(new LineListener() {

                    public void update(LineEvent event) {
                        if (event.getType() == LineEvent.Type.STOP) {
                            log.info("Play Done!");
                            if (isCalibratingAuditoryMap == true) {
                                cochleaCalibrateITDs[curCalibrationPoint] = lastCochleaPanOffset;
                                if (curCalibrationPoint < numCalibrationPoints - 1) {
                                    curCalibrationPoint++;
                                    startNextCalibration();
                                } else {
                                    log.info("Calibration of auditory map finished. writing to file...");
                                    try {
                                        FileWriter fstream = new FileWriter("AuditoryMap.dat");
                                        BufferedWriter ITDFile = new BufferedWriter(fstream);
                                        ITDFile.write("PanPos\tITD\n");
                                        for (int i = 0; i < numCalibrationPoints; i++) {
                                            ITDFile.write(cochleaCalibrateAngles[i] + "\t" + cochleaCalibrateITDs[i] + "\n");
                                        }
                                        ITDFile.close();
                                    } catch (Exception e) {
                                        System.err.println("Error: " + e.getMessage());
                                    }
                                    btnCalibrateCochlea.setEnabled(true);
                                    isCalibratingAuditoryMap = false;
                                    sendMessageToITDFilter(4, 0);
                                }
                            }
                            if (isCalibratingCochleaChannels == true) {
                                if (curCalibrationPoint < numCalibrationPoints - 1) {
                                    curCalibrationPoint++;
                                    startNextCalibration();
                                } else {
                                    log.info("Calibration of Channels finished!");
                                    isCalibratingCochleaChannels = false;
                                }
                            }
                        }
                    }
                });
            } catch (java.net.MalformedURLException e) {
            } catch (java.io.IOException e) {
            } catch (LineUnavailableException e) {
            } catch (UnsupportedAudioFileException e) {
            }
        }
    }
