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
        for (String s : files) {
            System.out.println(s);
        }
        int[] lengths = getLengths(files);
        BufferedReader[] elands = new BufferedReader[files.length];
        String[] lines = new String[files.length];
        String all_f = outName + ".all.eland";
        String sum_f = outName + ".summary";
        System.out.println("Writing to:\n" + all_f + "\n" + sum_f);
        FileWriter fw_all = null;
        FileWriter sum = null;
        try {
            fw_all = new FileWriter(all_f);
            sum = new FileWriter(sum_f);
        } catch (IOException io) {
            System.out.println("Can't initialize files!");
            System.out.println("Message thrown by Java environment (may be null):" + io.getMessage());
            System.exit(0);
        }
        assert (fw_all != null);
        assert (sum != null);
        for (int i = 0; i < elands.length; i++) {
            try {
                elands[i] = new BufferedReader(new FileReader(files[i]));
            } catch (FileNotFoundException e) {
                System.out.println("Can't find file: " + files[i]);
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
                    fw_all.write(alnToLine(lines[lines.length - 1], lengths[lines.length - 1]) + "\tQC\n");
                    continue;
                }
                if (hasNoMatch(lines[0])) {
                    try {
                        fw_all.write(alnToLine(lines[lines.length - 1], lengths[lines.length - 1]) + "\tNM\n");
                    } catch (IOException io) {
                        System.out.println("Warning sequence length not as long as line length: " + lines[lines.length - 1]);
                        System.out.println("Message thrown by Java environment (may be null):" + io.getMessage());
                        continue;
                    }
                    nmcnt++;
                    continue;
                }
                boolean umatch = false;
                for (int i = lines.length - 1; i >= 0; i--) {
                    if (hasUniqueMatch(lines[i])) {
                        fw_all.write(lines[i].substring(0, lines[i].indexOf('\t') + 1));
                        String seq = null;
                        seq = getSeq(lines[i], lengths[i]);
                        lenHisto[seq.length()]++;
                        fw_all.write(seq);
                        fw_all.write(lines[i].substring(lines[i].indexOf('\t', lines[i].indexOf(seq) + 1)));
                        fw_all.write("\n");
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
                        alnToLine(lines[i], lengths[i]);
                        fw_all.write(alnToLine(lines[i], lengths[i]) + "\tMM\n");
                        mmatch = true;
                        mmcnt++;
                        break;
                    }
                }
                if (!mmatch) {
                    System.out.println("Error! No matches?!?!");
                    for (String l : lines) System.out.println(l);
                }
            }
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
            System.out.printf("%n||Total reads |%d|%n||Unique Matches |%d|%n||Multimatches |%d|%n||No Match |%d|%n||Poor quality |%d|%n", total, umcnt, mmcnt, nmcnt, qccnt);
            fw_all.close();
            sum.close();
        } catch (IOException io) {
            System.out.println("Error Iterating on file");
            System.out.println("Message thrown by Java environment (may be null):" + io.getMessage());
        }
    }
