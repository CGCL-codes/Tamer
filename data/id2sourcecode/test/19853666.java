            public MyMergeThread(IndexWriter writer, MergePolicy.OneMerge merge) throws IOException {
                super(writer, merge);
                mergeThreadCreated = true;
            }
