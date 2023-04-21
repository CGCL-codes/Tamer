    public int receiveTo(String localPath) {
        File f;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        byte[] buffer = new byte[2048];
        int lastread = 0;
        f = new File(localPath);
        if (f.exists()) {
            System.out.println("FILE exists, will not overwrite");
            return -1;
        } else try {
            if (!f.createNewFile()) {
                System.out.println("CANT create FILE " + localPath);
                return -2;
            }
        } catch (IOException e) {
        }
        ;
        try {
            out = new BufferedOutputStream(new FileOutputStream(f));
        } catch (FileNotFoundException e) {
            System.out.println("CANT FIND FILE");
        }
        System.out.println("receiveTO: " + this.getPath());
        in = ftp.retr(this.getPath());
        if ((in == null) || (out == null)) {
            System.out.println("Ooops! Cant Set-Up File-Retreiving!");
            return -1;
        }
        try {
            for (int i = 0; (lastread = in.read(buffer, 0, 2048)) != -1; ) {
                out.write(buffer, 0, lastread);
                i += lastread;
                System.out.println(i);
            }
            ;
            if (ftp.getResult() != 226) return -1;
            in.close();
            out.close();
        } catch (IOException e) {
        }
        return 0;
    }
