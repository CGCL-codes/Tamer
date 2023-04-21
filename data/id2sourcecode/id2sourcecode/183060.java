    public static void main(String[] args) throws IOException {
        InputStream bitstream = null;
        InputStream orchestra = null;
        InputStream score = null;
        OutputStream output = null;
        int nOutputFormatIndex = DEFAULT_FORMAT;
        boolean bLineOutput = false;
        int c;
        Getopt g = new Getopt("saint", args, "hVb:c:s:o:f:");
        while ((c = g.getopt()) != -1) {
            switch(c) {
                case 'h':
                    printUsageAndExit();
                case 'V':
                    printVersionAndExit();
                case 'b':
                    bitstream = new FileInputStream(g.getOptarg());
                    break;
                case 'c':
                    orchestra = new FileInputStream(g.getOptarg());
                    break;
                case 's':
                    score = new FileInputStream(g.getOptarg());
                    break;
                case 'o':
                    if (g.getOptarg().equals("-")) {
                        output = System.out;
                    }
                    if (g.getOptarg().equals("+")) {
                        bLineOutput = true;
                    } else {
                        output = new FileOutputStream(g.getOptarg());
                    }
                    break;
                case 'f':
                    int nNewOutputFormatIndex = -1;
                    for (int i = 0; i < SUPPORTED_FORMATS.length; i++) {
                        if (SUPPORTED_FORMATS[i].getName().equals(g.getOptarg())) {
                            nNewOutputFormatIndex = i;
                        }
                    }
                    if (nNewOutputFormatIndex != -1) {
                        nOutputFormatIndex = nNewOutputFormatIndex;
                    } else {
                        System.err.println("warning: output format " + g.getOptarg() + "not supported; using default output format");
                    }
                    break;
            }
        }
        if (output == null && !bLineOutput) {
            System.out.println("no output specified!");
            printUsageAndExit();
        }
        Saint saint = null;
        if (bitstream != null) {
            saint = new Saint(bitstream);
        } else if (orchestra != null) {
            if (score != null) {
                saint = new Saint(orchestra, score);
            } else {
                System.out.println("no score file specified!");
                printUsageAndExit();
            }
        } else {
            System.out.println("neither bitstream nor orchestra specified!");
            printUsageAndExit();
        }
        System.err.println("output will be produces with " + saint.getChannelCount() + " channel(s) at " + saint.getSamplingRate() + " Hz\n");
        if (bLineOutput) {
            AudioFormat format = new AudioFormat(SUPPORTED_FORMATS[nOutputFormatIndex].getEncoding(), saint.getSamplingRate(), SUPPORTED_FORMATS[nOutputFormatIndex].getSampleSize(), saint.getChannelCount(), saint.getChannelCount() * SUPPORTED_FORMATS[nOutputFormatIndex].getSampleSize() / 8, saint.getSamplingRate(), SUPPORTED_FORMATS[nOutputFormatIndex].getBigEndian());
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine line = null;
            try {
                line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();
            } catch (LineUnavailableException e) {
            }
            output = new SourceDataLineOutputStream(line);
        }
        saint.setOutput(output, SUPPORTED_FORMATS[nOutputFormatIndex].getNumber());
        saint.run();
        try {
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
