    static void process(File inputfile, File outputfile, AudioFormat format, String script) throws Exception {
        if (!inputfile.exists()) {
            throw new Exception(" File not found " + inputfile);
        }
        try {
            AudioWriter audioWriter = new AudioWriter(outputfile, format);
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
