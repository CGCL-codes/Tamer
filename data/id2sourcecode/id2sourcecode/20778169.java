    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Parameters required: <output directory> <filename> <file 1> <file 2> ....");
            System.exit(0);
        }
        String output_directory = args[0];
        if (!output_directory.endsWith(System.getProperty("file.separator"))) {
            output_directory = output_directory.concat(System.getProperty("file.separator"));
        }
        LB = Log_Buffer.getLogBufferInstance();
        LB.addPrintStream(System.out);
        LB.addLogFile(output_directory + "CombineFiles.log");
        Thread th = new Thread(LB);
        th.start();
        new CurrentVersion(LB);
        LB.Version("CombineFiles", "$Revision: 3247 $");
        String output_file = output_directory + args[1];
        sizeof = args.length - 2;
        reads = new int[sizeof];
        CounterArray.set_sizeof(sizeof);
        String line = null;
        String key = null;
        int value = -1;
        int e = 0;
        reads = new int[sizeof];
        LB.notice("Processing Files...");
        for (int f = 2; f < args.length; f++) {
            e = f - 2;
            LB.notice("file " + (f - 1) + " : " + args[f]);
            FileOpen(args[f]);
            try {
                line = br.readLine();
                String[] fields = line.split(" ");
                reads[e] = Integer.valueOf(fields[0]);
            } catch (IOException io) {
                LB.error("Could not read first line of file " + args[f]);
                LB.error("Message thrown by Java environment (may be null):" + io.getMessage());
                LB.die();
            }
            try {
                while ((line = br.readLine()) != null) {
                    String[] fields = line.split(" ");
                    key = fields[0];
                    value = Integer.valueOf(fields[1]);
                    if (!Transcriptome.containsKey(key)) {
                        Transcriptome.put(key, new CounterArray());
                    }
                    Transcriptome.get(key).add_value(e, value);
                }
            } catch (IOException io) {
                LB.error("Could not process file " + args[f]);
                LB.error("Message thrown by Java environment (may be null):" + io.getMessage());
                LB.die();
            }
            try {
                br.close();
            } catch (IOException io) {
                LB.warning("Could not close file " + args[f]);
                LB.error("Message thrown by Java environment (may be null):" + io.getMessage());
                LB.die();
            }
        }
        LB.notice("Writing to disk...");
        LB.notice("Output to : " + output_file);
        FileOut FO = new FileOut(LB, output_file, false);
        Enumeration<String> keys = Transcriptome.keys();
        List<String> keyList = Collections.list(keys);
        Collections.sort(keyList);
        FO.write("Total");
        for (int r = 0; r < sizeof; r++) {
            FO.write("," + reads[r]);
        }
        FO.writeln("");
        for (String B : keyList) {
            FO.write(B);
            for (int v = 0; v < sizeof; v++) {
                FO.write("," + Transcriptome.get(B).get_value(v));
            }
            FO.writeln("");
        }
        FO.close();
        LB.close();
    }
