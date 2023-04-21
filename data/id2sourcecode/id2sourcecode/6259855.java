    @Primitive
    public static Value caml_gr_sound(final CodeRunner ctxt, final Value freq, final Value duration) throws Fail.Exception {
        GraphSlot.checkGraph(ctxt);
        if (ctxt.getContext().getParameters().isJavaxSoundUsed()) {
            try {
                final Beep bip = new Beep(Sound.SAMPLE_RATE, freq.asLong(), duration.asLong());
                final DataLine.Info info = new DataLine.Info(SourceDataLine.class, bip.getFormat());
                final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(bip.getFormat());
                line.start();
                final byte[] data = new byte[Sound.BUFFER_SIZE];
                while (bip.available() > 0) {
                    final int read = bip.read(data);
                    int written = line.write(data, 0, read);
                    while (written < read) {
                        written += line.write(data, written, read - written);
                    }
                }
                line.drain();
                line.close();
            } catch (final LineUnavailableException lue) {
                Toolkit.getDefaultToolkit().beep();
            } catch (final IOException ioe) {
            }
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
        return Value.UNIT;
    }
