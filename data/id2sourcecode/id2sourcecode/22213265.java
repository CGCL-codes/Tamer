    public static void main(String[] args) {
        System.out.println("<<<START>>>");
        try {
            File file = new File("E:/workspace/JAVA_mylibs/presrc/pretest.dat");
            FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
            InputStream is = Channels.newInputStream(channel);
            StreamMultiManipulatorBETA x = new StreamMultiManipulatorBETA(20);
            ArrayList<Finders> y = new ArrayList<Finders>();
            y.add(new Finder("tos", new Converter_tagToString()));
            y.add(new Finder("ttttteeeeesssssttttt"));
            y.add(new Finder("rin", new Converter_tagToString()));
            y.add(new Finder("test", new Converter_tagToString()));
            y.add(new Finder_HTMLTAG_ALFA(false, new Converter_delete()));
            OutputStreamWriter outW = new OutputStreamWriter(System.out);
            x.findAndReplace(is, y, outW);
            outW.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n<<<STOP>>>");
    }
