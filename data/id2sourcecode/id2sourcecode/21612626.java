    public void saveFile(String input, String output) throws IOException {
        FtpInputStream is = null;
        byte[] line = new byte[1];
        FtpFile file = new FtpFile("/" + path + "/" + input, cl);
        is = new FtpInputStream(file);
        BufferedInputStream br = new BufferedInputStream(is);
        File f = new File(output);
        f.createNewFile();
        FileOutputStream of = new FileOutputStream(f);
        while (br.read(line) != (-1)) of.write(line);
        of.close();
        br.close();
    }
