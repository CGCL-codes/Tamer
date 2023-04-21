    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        String compressParam = args[0];
        String fn = args[1];
        String ofn = args[2];
        boolean compress = true;
        if (compressParam.toLowerCase().startsWith("-c")) {
            compress = true;
        } else if (compressParam.toLowerCase().startsWith("-d")) {
            compress = false;
        } else {
            System.err.print("ERROR, first argument must be '-c' or '-d' for compressing or decompressing");
            System.exit(1);
        }
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        if (compress) {
            System.out.println("Compressing " + fn + " into " + ofn);
            FileStatus[] stati = fs.listStatus(new Path(fn));
            if (stati.length < 1) {
                System.err.println("Could not compress non-existent file:" + fn);
                System.exit(1);
            }
            if (fs.exists(new Path(ofn))) {
                System.err.println("could not write to " + ofn + " because the directory already exists.");
                System.exit(2);
            }
            if (!fs.mkdirs(new Path(ofn))) {
                System.err.println("Could not make the path " + ofn + ".");
                System.exit(3);
            }
            long fileSize = stati[0].getLen();
            int manualSplits = 9;
            int wordSize = 2;
            int blockSize = roundToWord((int) fileSize, wordSize);
            SequenceFileOutputFormat sfof = new SequenceFileOutputFormat();
            String[] tfn = new String[manualSplits];
            SequenceFile.Writer[] sw = new SequenceFile.Writer[manualSplits];
            JobConf prepJb = new JobConf();
            prepJb.setOutputFormat(SequenceFileOutputFormat.class);
            int stride = roundToWord(blockSize / manualSplits, wordSize);
            for (int offset = 0; offset < manualSplits; ++offset) {
                int ind = offset % manualSplits;
                tfn[ind] = "/tmp/hzip_tempfile_" + (new Random()).nextLong();
                sw[ind] = SequenceFile.createWriter(fs, prepJb, new Path(tfn[ind]), IntWritable.class, IntWritable.class);
            }
            for (int offset = 0; stride * offset < blockSize; ++offset) {
                int key, val;
                key = (stride * offset) / wordSize;
                if (fileSize - stride * offset >= stride) {
                    val = stride / wordSize;
                } else {
                    val = roundToWord((int) (fileSize - stride * offset), wordSize) / wordSize;
                }
                IntWritable kk = new IntWritable(key);
                IntWritable vv = new IntWritable(val);
                sw[offset % manualSplits].append(kk, vv);
            }
            for (int i = 0; i < manualSplits; ++i) sw[i].close();
            JobConf jb = new JobConf();
            jb.setInputFormat(SequenceFileInputFormat.class);
            jb.setOutputFormat(SequenceFileOutputFormat.class);
            jb.setCompressMapOutput(false);
            jb.setMapOutputKeyClass(IntWritable.class);
            jb.setMapOutputValueClass(BWCConfigurable.IntArrayWritable.class);
            jb.setOutputKeyClass(IntWritable.class);
            jb.setOutputValueClass(BytesWritable.class);
            jb.setMapperClass(BWCMapper.class);
            jb.setReducerClass(BWCReducer.class);
            jb.setCombinerClass(BWCCombiner.class);
            jb.setNumMapTasks((int) (Math.max(2, fileSize / blockSize)));
            jb.setNumTasksToExecutePerJvm(1);
            jb.setNumReduceTasks(1);
            jb.setNumTasksToExecutePerJvm(1);
            jb.setJar(new File(".").getAbsolutePath() + "/HZip.jar");
            DistributedCache.addCacheFile(new URI(fn), jb);
            jb.setInt(Common.CONFIG_BLOCK_SIZE, blockSize);
            jb.setInt(Common.CONFIG_WORD_SIZE, wordSize);
            jb.setStrings(Common.CONFIG_OUTPUT_PATH, ofn);
            jb.setInt(Common.INPUT_FILE_SIZE, (int) stati[0].getLen());
            for (int i = 0; i < manualSplits; ++i) FileInputFormat.addInputPath(jb, new Path(tfn[i]));
            String MROutputPath = ofn + "/intermediate_results";
            FileOutputFormat.setOutputPath(jb, new Path(MROutputPath));
            RunningJob jobResult = JobClient.runJob(jb);
            while (!jobResult.isComplete()) {
                System.out.println("waiting for the compress...");
                Thread.sleep(5000);
            }
            String bodyFile = ofn + "/body.data";
            logger.debug(" now moving file to:" + bodyFile);
            fs.rename(new Path(MROutputPath + "/part-00000"), new Path(bodyFile));
            fs.delete(new Path(MROutputPath), true);
            for (int i = 0; i < manualSplits; ++i) {
                fs.delete(new Path(tfn[i]), false);
            }
            System.out.println("Done.");
        } else {
            System.out.println("Decompressing " + ofn + " into " + fn);
        }
    }
