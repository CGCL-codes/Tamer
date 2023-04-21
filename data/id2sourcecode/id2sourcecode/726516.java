    public static void grab(int kz) {
        File file = new File((new StringBuilder()).append(dir).append("data.zip").toString());
        try {
            (new File(dir)).mkdir();
            file.createNewFile();
            System.out.println("Downloading updates...");
            URL url = new URL("http://updates.sourmud.jwarez.net/update" + kz + ".zip");
            DataInputStream datainputstream = new DataInputStream(url.openStream());
            FileOutputStream fileoutputstream = new FileOutputStream(file);
            byte abyte0[] = new byte[0x100000];
            boolean flag = false;
            int i;
            int j;
            for (j = 0; (i = datainputstream.read(abyte0)) > -1; j += i) {
                fileoutputstream.write(abyte0, 0, i);
                fileoutputstream.flush();
            }
            fileoutputstream.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
