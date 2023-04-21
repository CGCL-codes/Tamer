    public Bowed(final BowStringFactory factory, Patch patch) {
        this.factory = factory;
        nChannels = factory.chans();
        valueizers = new Vector<Valueizer>();
        String name = "Bowed";
        valueizers.add(velmap = new Valueizer("Velocity", name));
        valueizers.add(pressmap = new Valueizer("Pressure", name));
        valueizers.add(posmap = new Valueizer("Position ", name));
        valueizers.add(dampmap = new Valueizer("Damping", name));
        ModelOscillator osc = new ModelOscillator() {

            public float getAttenuation() {
                return 0;
            }

            public int getChannels() {
                return nChannels;
            }

            public ModelOscillatorStream open(float samplerate) {
                return lastOsc = new MyOscillatorStream(samplerate);
            }
        };
        ModelPerformer performer = new ModelPerformer();
        performer.getOscillators().add(osc);
        performer.getConnectionBlocks().add(new ModelConnectionBlock(Double.NEGATIVE_INFINITY, new ModelDestination(new ModelIdentifier("eg", "attack", 0))));
        performer.getConnectionBlocks().add(new ModelConnectionBlock(0.0, new ModelDestination(new ModelIdentifier("eg", "hold", 0))));
        performer.getConnectionBlocks().add(new ModelConnectionBlock(0.0, new ModelDestination(new ModelIdentifier("eg", "decay", 0))));
        performer.getConnectionBlocks().add(new ModelConnectionBlock(1000.0, new ModelDestination(new ModelIdentifier("eg", "sustain", 0))));
        performer.getConnectionBlocks().add(new ModelConnectionBlock(1200.0, new ModelDestination(new ModelIdentifier("eg", "release", 0))));
        SimpleInstrument ins = new BowedInstrument();
        ins.setName(factory.getPatchName());
        ins.add(performer);
        ins.setPatch(patch);
        addInstrument(ins);
    }
