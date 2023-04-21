    public Testsine() throws ParserConfigurationException, SAXException, IOException {
        Runtime sR = Runtime.getRuntime();
        InputStream streamF = getClass().getResourceAsStream("FokaoSynthies.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        try {
            Document doc = db.parse(streamF);
            System.out.println(streamF.toString() + " Found");
            Element documentElement = doc.getDocumentElement();
            NodeList nodeLst = documentElement.getElementsByTagName("Instruments");
            if (nodeLst.getLength() == 1) {
                Element Instruments = (Element) nodeLst.item(0);
                NodeList InstrumentsList = Instruments.getElementsByTagName("Instrument");
                int iPatch = 0;
                for (int iInst = 0; iInst < InstrumentsList.getLength(); iInst++) {
                    Element Instrument = (Element) InstrumentsList.item((iInst));
                    iPatch = iInst;
                    String Name = "<noname>";
                    if (Instrument.getElementsByTagName("Name").getLength() == 1) {
                        Name = ((Element) Instrument.getElementsByTagName("Name").item(0)).getTextContent();
                    }
                    if (Instrument.getElementsByTagName("Patch").getLength() == 1) {
                        iPatch = Integer.parseInt(((Element) Instrument.getElementsByTagName("Patch").item(0)).getTextContent());
                    }
                    SimpleInstrument ins = new SimpleInstrument();
                    MyModelOscillator osc = new MyModelOscillator();
                    if (Instrument.getElementsByTagName("Generators").getLength() == 1) {
                        Element GeneratorsList = (Element) Instrument.getElementsByTagName("Generators").item(0);
                        if (GeneratorsList.getElementsByTagName("Generator").getLength() > 0) {
                            NodeList GeneratorList = GeneratorsList.getElementsByTagName("Generator");
                            for (int iGen = 0; iGen < GeneratorList.getLength(); iGen++) {
                                Element Generator = (Element) GeneratorList.item((iGen));
                                Wavedata sData = new Wavedata();
                                String sGeneratorType = Generator.getAttribute("Type").toUpperCase();
                                if (sGeneratorType.equals("WHITENOISE")) {
                                    sData.sType = WAVETYPE.WhiteNoise;
                                }
                                if (sGeneratorType.equals("RECT")) {
                                    sData.sType = WAVETYPE.Rect;
                                }
                                if (sGeneratorType.equals("SAWTOOTH")) {
                                    sData.sType = WAVETYPE.Sawtooth;
                                }
                                if (sGeneratorType.equals("BACKSAWTOOTH")) {
                                    sData.sType = WAVETYPE.BackSawtooth;
                                }
                                if (Generator.getElementsByTagName("GeneratorFreqFactor").getLength() == 1) {
                                    Element GeneratorFreqFactor = ((Element) Generator.getElementsByTagName("GeneratorFreqFactor").item(0));
                                    sData.FreqFactor = Float.parseFloat(GeneratorFreqFactor.getTextContent());
                                }
                                if (Generator.getElementsByTagName("GeneratorVolFactor").getLength() == 1) {
                                    Element GeneratorVolFactor = ((Element) Generator.getElementsByTagName("GeneratorVolFactor").item(0));
                                    sData.VolumeFactor = Float.parseFloat(GeneratorVolFactor.getTextContent());
                                }
                                osc.theData.add(sData);
                            }
                        }
                    }
                    if (osc.theData.size() == 0) {
                        Wavedata sData = new Wavedata();
                        osc.theData.add(sData);
                    }
                    ModelPerformer performer = new ModelPerformer();
                    performer.getOscillators().add(osc);
                    if (Instrument.getElementsByTagName("Envelope").getLength() == 1) {
                        Element Envelope = ((Element) Instrument.getElementsByTagName("Envelope").item(0));
                        double dVal = 0.0;
                        if (Envelope.getElementsByTagName("Delay").getLength() == 1) {
                            Element Attack = ((Element) Envelope.getElementsByTagName("Delay").item(0));
                            dVal = Double.parseDouble(Attack.getTextContent());
                            if (dVal <= -10000) dVal = Double.NEGATIVE_INFINITY;
                        } else {
                            dVal = Double.NEGATIVE_INFINITY;
                        }
                        performer.getConnectionBlocks().add(new ModelConnectionBlock(dVal, new ModelDestination(new ModelIdentifier("eg", "delay", 0))));
                        if (Envelope.getElementsByTagName("Attack").getLength() == 1) {
                            Element Attack = ((Element) Envelope.getElementsByTagName("Attack").item(0));
                            dVal = Double.parseDouble(Attack.getTextContent());
                            if (dVal <= -10000) dVal = Double.NEGATIVE_INFINITY;
                        } else {
                            dVal = Double.NEGATIVE_INFINITY;
                        }
                        performer.getConnectionBlocks().add(new ModelConnectionBlock(dVal, new ModelDestination(new ModelIdentifier("eg", "attack", 0))));
                        if (Envelope.getElementsByTagName("Hold").getLength() == 1) {
                            Element Attack = ((Element) Envelope.getElementsByTagName("Hold").item(0));
                            dVal = Double.parseDouble(Attack.getTextContent());
                        } else {
                            dVal = 0.0;
                        }
                        performer.getConnectionBlocks().add(new ModelConnectionBlock(dVal, new ModelDestination(new ModelIdentifier("eg", "hold", 0))));
                        if (Envelope.getElementsByTagName("Decay").getLength() == 1) {
                            Element Attack = ((Element) Envelope.getElementsByTagName("Decay").item(0));
                            dVal = Double.parseDouble(Attack.getTextContent());
                        } else {
                            dVal = 0.0;
                        }
                        performer.getConnectionBlocks().add(new ModelConnectionBlock(dVal, new ModelDestination(new ModelIdentifier("eg", "decay", 0))));
                        if (Envelope.getElementsByTagName("Sustain").getLength() == 1) {
                            Element Attack = ((Element) Envelope.getElementsByTagName("Sustain").item(0));
                            dVal = Double.parseDouble(Attack.getTextContent());
                        } else {
                            dVal = 0.0;
                        }
                        performer.getConnectionBlocks().add(new ModelConnectionBlock(dVal, new ModelDestination(new ModelIdentifier("eg", "sustain", 0))));
                        if (Envelope.getElementsByTagName("Release").getLength() == 1) {
                            Element Attack = ((Element) Envelope.getElementsByTagName("Release").item(0));
                            dVal = Double.parseDouble(Attack.getTextContent());
                        } else {
                            dVal = 0.0;
                        }
                        performer.getConnectionBlocks().add(new ModelConnectionBlock(dVal, new ModelDestination(new ModelIdentifier("eg", "release", 0))));
                        if (Envelope.getElementsByTagName("Shutdown").getLength() == 1) {
                            Element Attack = ((Element) Envelope.getElementsByTagName("Shutdown").item(0));
                            dVal = Double.parseDouble(Attack.getTextContent());
                        } else {
                            dVal = 0.0;
                        }
                        performer.getConnectionBlocks().add(new ModelConnectionBlock(dVal, new ModelDestination(new ModelIdentifier("eg", "shutdown", 0))));
                    }
                    System.out.println("Instrument[" + iPatch + "]: " + Name);
                    ins.setName(Name);
                    ins.add(performer);
                    ins.setPatch(new Patch(0, iPatch));
                    addInstrument(ins);
                }
            } else {
                System.out.println("misformed XML or No Instruments found");
            }
        } catch (java.io.FileNotFoundException sFN) {
            JOptionPane.showMessageDialog(null, sFN.getLocalizedMessage());
            SimpleInstrument ins = new SimpleInstrument();
            ModelOscillator osc = new ModelOscillator() {

                public float getAttenuation() {
                    return 0;
                }

                public int getChannels() {
                    return 1;
                }

                public ModelOscillatorStream open(float samplerate) {
                    return new MyOscillatorStream(samplerate, null);
                }
            };
            ModelPerformer performer = new ModelPerformer();
            performer.getOscillators().add(osc);
            performer.getConnectionBlocks().add(new ModelConnectionBlock(Double.NEGATIVE_INFINITY, new ModelDestination(new ModelIdentifier("eg", "attack", 0))));
            performer.getConnectionBlocks().add(new ModelConnectionBlock(1000.0, new ModelDestination(new ModelIdentifier("eg", "sustain", 0))));
            performer.getConnectionBlocks().add(new ModelConnectionBlock(1200.0, new ModelDestination(new ModelIdentifier("eg", "release", 0))));
            System.out.println("Instrument[0]: Fokao Test Sine");
            ins.setName("Fokao Test Sine");
            ins.add(performer);
            ins.setPatch(new Patch(0, 0));
            addInstrument(ins);
        }
    }
