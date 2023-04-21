    public static void main(final String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java tgreiner.amy.chess.tablebases.Main <tb>");
            System.exit(1);
        }
        Classifier c = new Classifier();
        Handle h = c.classify(args[0]);
        h.normalize();
        String tb = h.toString();
        IndexerFactory factory = new IndexerFactory();
        Indexer indexer = factory.createIndexer(h);
        if (indexer == null) {
            System.err.println("Unknown table base " + tb);
            System.exit(1);
        }
        int count = indexer.getPositionCount();
        File fWhite = new File("temp.tbw");
        File fBlack = new File("temp.tbb");
        RandomAccessFile fileWhite = new RandomAccessFile(fWhite, "rw");
        RandomAccessFile fileBlack = new RandomAccessFile(fBlack, "rw");
        fileWhite.setLength(count);
        fileBlack.setLength(count);
        MappedByteBuffer bufferWhite = fileWhite.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, count);
        MappedByteBuffer bufferBlack = fileBlack.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, count);
        Loader l = new Loader();
        Generator gen = new Generator(h, bufferWhite, bufferBlack, l.load());
        gen.generate();
        bufferWhite.force();
        bufferBlack.force();
        fileWhite.close();
        fileBlack.close();
        String whiteFileName = "tb" + File.separator + tb + ".tbw";
        String blackFileName = "tb" + File.separator + tb + ".tbb";
        fWhite.renameTo(new File(whiteFileName));
        fBlack.renameTo(new File(blackFileName));
    }
