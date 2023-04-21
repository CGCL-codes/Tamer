    private void readwrite(IceParserTransducer transducer, String inputFileAndPath, String outputFileAndPath) throws IOException {
        BufferedReader br;
        BufferedWriter bw;
        StringReader sr;
        StringWriter sw;
        String str;
        br = FileEncoding.getReader(inputFileAndPath);
        bw = FileEncoding.getWriter(outputFileAndPath);
        while ((str = br.readLine()) != null) {
            sr = new StringReader(str);
            sw = new StringWriter();
            transducer.yyclose();
            transducer.yyreset(sr);
            transducer.parse(sw);
            bw.write(sw.toString());
            bw.write("\n");
        }
        bw.flush();
        bw.close();
    }
