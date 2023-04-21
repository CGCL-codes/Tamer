    public static void printFile(String infile, String out, int read_length) throws IOException {
        Path thePath = new Path(infile);
        Configuration conf = new Configuration();
        SequenceFile.Reader theReader = new SequenceFile.Reader(FileSystem.get(conf), thePath, conf);
        Text key = new Text();
        BytesWritable value = new BytesWritable();
        FileWriter fw = new FileWriter(out);
        byte[] readline;
        while (theReader.next(key, value)) {
            fw.write(key.toString());
            fw.write("\n");
            readline = decompress(value.getBytes(), read_length);
            for (int i = 0; i < readline.length; i++) fw.write(readline[i]);
            fw.write("\n");
        }
        fw.close();
    }
