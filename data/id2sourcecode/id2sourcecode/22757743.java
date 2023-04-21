    public boolean launch() {
        try {
            String path = "http://www.fruitfly.org/cgi-bin/annot/launch.pl?seqid=" + getInputSequence().getName() + "&residues=" + getInputSequence().getResidues();
            URL url = new URL(path);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String ticket = in.readLine();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
