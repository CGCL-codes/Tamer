    private static char[] readChars(File file) {
        CharArrayWriter chararraywriter = new CharArrayWriter();
        try {
            FileReader filereader = new FileReader(file);
            BufferedReader bufferedreader = new BufferedReader(filereader);
            int i = 0;
            char ac[] = new char[16384];
            while ((i = bufferedreader.read(ac)) != -1) if (i > 0) chararraywriter.write(ac, 0, i);
            bufferedreader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return chararraywriter.toCharArray();
    }
