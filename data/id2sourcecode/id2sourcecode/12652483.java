    public byte[] encodeText(Reader reader) {
        if (reader == null) {
            return null;
        }
        byte[] fileDigest = null;
        try {
            ReaderInputStream ris = new ReaderInputStream(reader);
            BufferedReader br = new BufferedReader(new InputStreamReader(ris));
            this.md.reset();
            String riga = "";
            while ((riga = br.readLine()) != null) {
                riga += Utility.LINE_SEP;
                this.md.update(riga.getBytes());
            }
            ris.close();
            ris = null;
            fileDigest = this.md.digest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fileDigest;
    }
