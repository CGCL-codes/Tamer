    public Marc21Load(String sFileName) {
        try {
            File f = new File(sFileName);
            FileInputStream fis = new FileInputStream(f);
            FileChannel fch = fis.getChannel();
            ByteArrayOutputStream baos = new ByteArrayOutputStream((int) f.length());
            WritableByteChannel wbc = Channels.newChannel(baos);
            long pos = 0;
            long cnt = 0;
            while ((cnt = fch.transferTo(pos, f.length(), wbc)) > 0) {
                pos += cnt;
            }
            fis.close();
            this.abmarc = baos.toByteArray();
            int start = 0;
            for (int i = 1; i < this.abmarc.length; i++) {
                if (this.abmarc[i] == 0x1D) {
                    ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                    try {
                        baos2.write(this.abmarc, start, i - 1 - start);
                    } catch (Exception ex) {
                        System.exit(0);
                    }
                    lstmarc.add(baos2.toByteArray());
                    start = ++i;
                }
            }
            String tmpmarc = new String(abmarc, "US-ASCII");
            this.admarcdescription = new Marc21Description[this.lstmarc.size()];
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }
