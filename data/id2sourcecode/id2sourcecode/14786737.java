    public void index() throws SearchEngineException {
        try {
            ramwriter = new IndexWriter(ramDir, new ISFAnalyzer(), true);
            ramwriter.setUseCompoundFile(false);
            indexFile(id, reader, port, policyInfo, boost, ramwriter);
            ramwriter.close();
            WorkFlow.addWork(new MergeMemTask(ramDir));
        } catch (IOException e) {
            e.printStackTrace();
            throw new SearchEngineException("建立索引时出现数据访问错误");
        }
    }
