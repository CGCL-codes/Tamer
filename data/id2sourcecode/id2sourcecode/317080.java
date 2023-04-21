    public String readfile(String path, int interval) {
        try {
            filepath = path;
            File file = new File(filepath);
            FileReader fileread = new FileReader(file);
            bufread = new BufferedReader(fileread);
            int i = 0;
            int j = 0;
            while ((read = bufread.readLine()) != null) {
                readStr = readStr + read + "\n";
                i++;
                if (i == interval) {
                    j++;
                    String tname = "" + name + j;
                    writefile(tname, readStr, false);
                    System.out.println("wirte file:" + j);
                    readStr = "";
                    i = 0;
                }
            }
        } catch (Exception d) {
            System.out.println(d.getMessage());
        }
        return readStr;
    }
