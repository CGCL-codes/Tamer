    public void createTempIndex(File rootDocDir) {
        LuceneInterface _lf = new LuceneInterface();
        System.out.println("index dir ==>" + _lf._indexFile.getAbsolutePath());
        if (rootDocDir.isDirectory()) {
            String[] files = rootDocDir.list();
            Arrays.sort(files);
            for (int i = 0; i < files.length; i++) {
                String date = files[i].toString();
                File dateDir = new File(rootDocDir, files[i].toString());
                String[] dateFiles = dateDir.list();
                Arrays.sort(dateFiles);
                for (int j = 0; j < dateFiles.length; j++) {
                    NewsItem ni = new NewsItem();
                    File docFile = new File(dateDir, dateFiles[j]);
                    ni.SetLocalPath(docFile.getAbsolutePath());
                    ni.SetDate(date);
                    ni.SetSource(ni._sourceId);
                    ni.SetURL("www.rediff.com");
                    System.out.println(" patth ==>" + ni.GetLocalCopyPath());
                    System.out.println(" date ==>" + ni.GetDateString());
                    System.out.println(" source ==>" + ni.GetSourceId());
                    _lf.AddNewsItemToIndex(ni);
                }
            }
        }
    }
