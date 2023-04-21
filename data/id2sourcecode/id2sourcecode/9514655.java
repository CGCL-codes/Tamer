    public static void main(String args[]) throws Exception {
        File inputfile = new File("/home/pjl/massive.wav");
        File outputfile = new File("/home/pjl/output_wav_file.wav");
        String reverbfile = "/home/pjl/frinika/impulses/Deep Space.wav";
        if (!inputfile.exists()) {
            throw new Exception(" File not found " + inputfile);
        }
        File fil = new File(reverbfile);
        assert (fil.exists());
        AudioFormat format = new AudioFormat(44100, 16, 2, true, false);
        try {
            AudioWriter audioWriter = new AudioWriter(outputfile, format);
            String script = "output <- resample(2) <- file(input);";
            System.out.println(script);
            Interpreter interpreter = new Interpreter();
            interpreter.add("input", inputfile.getPath());
            interpreter.eval(script);
            Variable output = interpreter.get("output");
            AudioSession audiosession = new AudioSession(format.getSampleRate(), format.getChannels());
            AudioInputStream audiostream = audiosession.asByteStream(output, format);
            long bytes_to_render = inputfile.length();
            byte[] buffer = new byte[512];
            long writeout = 0;
            while (writeout < bytes_to_render) {
                int ret = -1;
                ret = audiostream.read(buffer);
                if (ret == -1) {
                    System.out.println(" ret == -1 ");
                    break;
                }
                writeout += ret;
                System.out.println(" rendered : " + writeout);
                audioWriter.write(buffer, 0, ret);
            }
            audioWriter.close();
            audiostream.close();
            audiosession.close();
            interpreter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ScriptParserException e1) {
            e1.printStackTrace();
        }
    }
