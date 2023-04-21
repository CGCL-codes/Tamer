    protected void readBinaryMarcFromFile() {
        try {
            File f = new File(fileName);
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
            this.bmarc = baos.toByteArray();
        } catch (FileNotFoundException fnfex) {
            fnfex.printStackTrace();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }
