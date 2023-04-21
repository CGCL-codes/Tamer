            public void open(AudioFormat format, int bufferSize) throws LineUnavailableException {
                this.format = format;
                buf = ByteBuffer.allocate(bufferSize).order(format.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
                flView = buf.asFloatBuffer();
                floatBuffer = new float[flView.capacity()];
                try {
                    if (vst == null) {
                        if (vstiFile == null) {
                            JFileChooser fileChooser = new JFileChooser();
                            fileChooser.showDialog(null, "Select VSTi");
                            vstiFile = fileChooser.getSelectedFile();
                        }
                        vst = JVstHost2.newInstance(vstiFile, format.getSampleRate(), flView.capacity());
                        if (programChunk != null) vst.setProgramChunk(programChunk);
                        vst.addJVstHostListener(new AbstractJVstHostListener() {

                            @Override
                            public void onAudioMasterAutomate(JVstHost2 vst, int index, float value) {
                                if (gui != null) {
                                    gui.updateParameter(index, value);
                                }
                            }
                        });
                        System.out.println("vst blocksize: " + vst.getBlockSize());
                        if (vst.hasEditor()) {
                            vst.openEditor("VST GUI");
                        } else {
                            gui = new StringGui(vst);
                            gui.setVisible(true);
                        }
                        fInputs = new float[vst.numInputs()][flView.capacity() / format.getChannels()];
                        fOutputs = new float[vst.numOutputs()][flView.capacity() / format.getChannels()];
                        isOpen = true;
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(FrinikaJVSTSynth.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JVstLoadException ex) {
                    Logger.getLogger(FrinikaJVSTSynth.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
