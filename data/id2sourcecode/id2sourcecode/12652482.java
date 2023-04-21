    public byte[] encodeText(File file) {
        if (file == null) {
            return null;
        }
        byte[] fileDigest = null;
        try {
            if (!file.canRead()) {
                return null;
            }
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            String riga = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            this.md.reset();
            while ((riga = br.readLine()) != null) {
                riga += Utility.LINE_SEP;
                this.md.update(riga.getBytes());
            }
            fis.close();
            fis = null;
            fileDigest = this.md.digest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fileDigest;
    }
