    protected String getResiduesFromSourceImpl(int low, int high) {
        String out = "";
        try {
            String host = "srs.sanger.ac.uk";
            int port = 80;
            String baseId = getDisplayId();
            int dotInd;
            if ((dotInd = baseId.lastIndexOf('.')) > 0) {
                baseId = baseId.substring(0, dotInd);
            }
            String urlstr = "http://" + host + ":" + port + "/srs6bin/cgi-bin/wgetz?-f+AccNumber+-f+Sequence+-sf+fasta+[embl-sv:\"" + getDisplayId() + "\"]|[embl-AccNumber:\"" + getDisplayId() + "\"]|[swall-AccNumber:\"" + baseId + "\"]";
            URL url = new URL(urlstr);
            URLConnection urlconn = url.openConnection();
            DataInputStream in = new DataInputStream(urlconn.getInputStream());
            System.out.println("in = " + in);
            String data;
            try {
                boolean hadId = false;
                while ((data = in.readLine()) != null) {
                    data = HTMLUtil.removeHtml(data);
                    if (data.indexOf("AC ") == 0) {
                        if (data.indexOf(baseId) > -1) {
                            data = in.readLine();
                        }
                        out += data + "\n";
                        ;
                        hadId = true;
                    } else if (hadId) {
                        out += data + "\n";
                        ;
                    }
                    System.out.println("Dehtmledline = " + data);
                }
            } catch (IOException ioex) {
                System.out.println("Exception " + ioex);
            }
            in.close();
        } catch (MalformedURLException ex) {
            System.out.println("Exception " + ex);
        } catch (IOException ioex) {
            System.out.println("Exception " + ioex);
        } catch (Exception ex) {
            System.out.println("Exception " + ex);
            ex.printStackTrace();
        }
        System.out.println("FASTA string = " + out);
        FastaFile fa = new FastaFile(out);
        return ((SequenceI) fa.seqs.elementAt(0)).getResidues(low + 1, high + 1);
    }
