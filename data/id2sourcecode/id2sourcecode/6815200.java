    public static void main(String[] args) {
        System.out.println("<<<START>>>");
        try {
            File file = new File("E:/workspace/JAVA_mylibs/presrc/multimanipulators/lp_es_ES_V2.0.xsl");
            FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
            InputStream is = Channels.newInputStream(channel);
            StreamMultiManipulatorBETA x = new StreamMultiManipulatorBETA(20);
            ArrayList<Finders> y = new ArrayList<Finders>();
            y.add(new Finder("\r\n", new Converter_delete()));
            y.add(new Finder("]", new Converter_delete()));
            y.add(new Finder("#", new Converter_delete()));
            y.add(new Finder_HTMLTAG_ALFA(false, new Converter_replaceString(" -%- ")));
            String encoding = "GB18030";
            File textFile = new File("E:/workspace/JAVA_mylibs/presrc/multimanipulators/lp_es_ES_V2.0.txt");
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(textFile));
            x.findAndReplace(is, y, writer);
            writer.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n<<<STOP:TEST>>>");
    }
