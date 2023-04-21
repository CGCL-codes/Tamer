    public void downloadFile(String chr, int start, int stop, boolean allFeatures) throws IOException {
        chrom = chr;
        _global_start = start;
        this.stop = stop;
        URL url = null;
        try {
            if (allFeatures) url = new URL("http://www.ensembl.org/Homo_sapiens/exportview?tab=embl&out=text&" + "chr=" + chr + "&bp_start=" + start + "&type=basepairs&embl_format=embl&" + "embl_repeat=on&embl_contig=on&" + "ftype=gene&btnsubmit=Export&bp_end=" + stop); else url = new URL("http://www.ensembl.org/Homo_sapiens/exportview?tab=embl&out=text&" + "chr=" + chr + "&bp_start=" + start + "&type=basepairs&embl_format=embl&" + "embl_contig=on&" + "ftype=gene&btnsubmit=Export&bp_end=" + stop);
            if (!cacheDirVerified) {
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            DataOutputStream outstr = new DataOutputStream(new FileOutputStream(fileName));
            String inputLine;
            inputLine = in.readLine();
            if (!inputLine.startsWith("ID")) {
                throw (new IOException("Bad Ensembl Download"));
            }
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.startsWith("SQ")) break;
                outstr.writeBytes(inputLine + "\n");
            }
            in.close();
            outstr.close();
        } catch (Exception exc) {
        }
    }
