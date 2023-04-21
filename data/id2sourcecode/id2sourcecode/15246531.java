    public static void main(String[] args) {
        int nmcnt = 0;
        int mmcnt = 0;
        int umcnt = 0;
        int qccnt = 0;
        int total = 0;
        int[] lenHisto = new int[100];
        if (args.length < 2) {
            System.out.println("usage: <output file name> <eland files>");
            System.exit(0);
        }
        String outName = args[0];
        String[] files = new String[args.length - 1];
        for (int i = 0; i < files.length; i++) {
            files[i] = args[i + 1];
        }
        System.out.println("Working with:");
        for (String s : files) System.out.println(s);
        int[] lengths = null;
        lengths = getLengths(files);
        BufferedReader[] elands = new BufferedReader[files.length];
        String[] lines = new String[files.length];
        String um_f = outName + ".um.eland";
        String nm_f = outName + ".nm.eland";
        String mm_f = outName + ".mm.eland";
        String qc_f = outName + ".qc.eland";
        String sum_f = outName + ".summary";
        System.out.println("Writing to:\n" + um_f + "\n" + nm_f + "\n" + mm_f + "\n" + qc_f + "\n" + sum_f);
        FileWriter um = null;
        FileWriter nm = null;
        FileWriter mm = null;
        FileWriter qc = null;
        FileWriter sum = null;
        try {
            um = new FileWriter(um_f);
            nm = new FileWriter(nm_f);
            mm = new FileWriter(mm_f);
            qc = new FileWriter(qc_f);
            sum = new FileWriter(sum_f);
        } catch (IOException io) {
            System.out.println("Can't create files.  Exiting");
            System.out.println("Message thrown by Java environment (may be null):" + io.getMessage());
            System.exit(0);
        }
        assert (um != null);
        assert (nm != null);
        assert (mm != null);
        assert (qc != null);
        assert (sum != null);
        for (int i = 0; i < elands.length; i++) {
            try {
                elands[i] = new BufferedReader(new FileReader(files[i]));
            } catch (FileNotFoundException FNF) {
                System.out.println("Could not open a file provided: " + files[i]);
            }
        }
        try {
            while (elands[0].ready()) {
                total++;
                if (total % 10000 == 0) {
                    System.out.print(".");
                }
                for (int i = 0; i < elands.length; i++) {
                    lines[i] = elands[i].readLine();
                }
                if (!sameRead(lines)) {
                    System.out.println("reads are not the same!");
                    write(lines);
                    System.exit(0);
                }
                if (hasQCerror(lines[0])) {
                    qccnt++;
                    qc.write(alnToFasta(lines[lines.length - 1], lengths[lines.length - 1]) + "\n");
                    continue;
                }
                if (hasNoMatch(lines[0])) {
                    nm.write(alnToFasta(lines[lines.length - 1], lengths[lines.length - 1]) + "\n");
                    nmcnt++;
                    continue;
                }
                boolean umatch = false;
                for (int i = lines.length - 1; i >= 0; i--) {
                    if (hasUniqueMatch(lines[i])) {
                        um.write(lines[i].substring(0, lines[i].indexOf('\t') + 1));
                        String seq = null;
                        seq = getSeq(lines[i], lengths[i]);
                        lenHisto[seq.length()]++;
                        um.write(seq);
                        um.write(lines[i].substring(lines[i].indexOf('\t', lines[i].indexOf(seq) + 1)));
                        um.write("\n");
                        umatch = true;
                        umcnt++;
                        break;
                    }
                }
                if (umatch) {
                    continue;
                }
                boolean mmatch = false;
                for (int i = lines.length - 1; i >= 0; i--) {
                    if (hasMultiMatch(lines[i])) {
                        alnToFasta(lines[i], lengths[i]);
                        mm.write(alnToFasta(lines[i], lengths[i]) + "\n");
                        mmatch = true;
                        mmcnt++;
                        break;
                    }
                }
                if (!mmatch) {
                    System.out.println("Error! No matches?!?!");
                    write(lines);
                }
            }
        } catch (IOException io) {
            System.out.println("Failure parsing file.");
            System.out.println("Message thrown by Java environment (may be null):" + io.getMessage());
        }
        try {
            sum.write("total\t" + total + "\n");
            sum.write("unique\t" + umcnt + "\n");
            sum.write("multi\t" + mmcnt + "\n");
            sum.write("none\t" + nmcnt + "\n");
            sum.write("QC\t" + qccnt + "\n");
            for (int i = 0; i < lenHisto.length; i++) {
                if (lenHisto[i] != 0) {
                    sum.write(i + "\t" + lenHisto[i] + "\n");
                }
            }
        } catch (IOException io) {
            System.out.println("Failure writing to files.");
            System.out.println("Message thrown by Java environment (may be null):" + io.getMessage());
        }
        System.out.printf("%n||Total reads |%d|%n||Unique Matches |%d|%n||Multimatches |%d|%n||No Match |%d|%n||Poor quality |%d|%n", total, umcnt, mmcnt, nmcnt, qccnt);
        try {
            um.close();
            nm.close();
            mm.close();
            sum.close();
            qc.close();
        } catch (IOException io) {
            System.out.println("Failure closing files.");
            System.out.println("Message thrown by Java environment (may be null):" + io.getMessage());
        }
    }
