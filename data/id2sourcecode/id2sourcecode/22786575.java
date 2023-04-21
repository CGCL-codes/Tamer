        @Override
        public void run() {
            try {
                while (run) {
                    if (type == 0) {
                        int i = seq.addAndGet(1);
                        Document doc = TestIndexWriterReader.createDocument(i, "index1", 10);
                        writer.addDocument(doc);
                        addCount++;
                    } else if (type == 1) {
                        IndexReader reader = writer.getReader();
                        int id = random.nextInt(seq.intValue());
                        Term term = new Term("id", Integer.toString(id));
                        int count = TestIndexWriterReader.count(term, reader);
                        writer.deleteDocuments(term);
                        reader.close();
                        delCount += count;
                    }
                }
            } catch (Throwable ex) {
                ex.printStackTrace(System.out);
                this.ex = ex;
                run = false;
            }
        }
