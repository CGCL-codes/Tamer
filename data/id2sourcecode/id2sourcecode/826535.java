    public String[][] blast(Screen sc, Seq s) throws Exception {
        String database;
        String program;
        if (s.getNature() == Nature.DNA) {
            database = sc.get_dataN();
            program = "blastn";
        } else {
            database = sc.get_dataP();
            program = "blastp";
        }
        try {
            FileWriter fstream = new FileWriter(file_in);
            BufferedWriter out = new BufferedWriter(fstream);
            bb[num_frame][num_sub_sequence].write("sequence blasted: " + s.getSymbolList().seqString());
            bb[num_frame][num_sub_sequence].newLine();
            bb[num_frame][num_sub_sequence].write("Blast results:");
            bb[num_frame][num_sub_sequence].newLine();
            out.write(">seq");
            out.newLine();
            out.write(s.getSymbolList().seqString());
            out.close();
            fstream.close();
        } catch (IOException e) {
            System.out.println("**************************ERROR**************************");
            System.out.println("error while writing the sequence on file");
            System.out.println("sequence " + s.getSymbolList().seqString());
            throw e;
        }
        try {
            String[] commands = { "sh", "-c", "mpirun -np $NSLOTS mpiblast -d " + database + " -i " + file_in + " -p " + program + " -o " + file_out + " -F F -e 1e-3 -m 9 --removedb" };
            Process child = Runtime.getRuntime().exec(commands);
            child.waitFor();
            child.getErrorStream().close();
            child.getInputStream().close();
            child.getOutputStream().close();
            child.destroy();
        } catch (InterruptedException e) {
            System.out.println("**************************ERROR**************************");
            System.out.println("interuption while executing the blast request");
            throw e;
        }
        ExtractMPI res = new ExtractMPI(1, 1, 1, 0, 0, 0, num_frame, num_sub_sequence, bb);
        return res.extract(file_out, s.getNature());
    }
