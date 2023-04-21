        @Override
        protected MergeThread getMergeThread(IndexWriter writer, MergePolicy.OneMerge merge) throws IOException {
            MergeThread thread = new MyMergeThread(writer, merge);
            thread.setThreadPriority(getMergeThreadPriority());
            thread.setDaemon(true);
            thread.setName("MyMergeThread");
            return thread;
        }
