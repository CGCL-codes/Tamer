    private static QDataSet test0_rank2_0() throws IOException, StreamException {
        URL url = TestQDataSetStreamHandler.class.getResource("test0_rank2_1.qds");
        QDataSetStreamHandler handler = new QDataSetStreamHandler();
        StreamTool.readStream(Channels.newChannel(url.openStream()), handler);
        QDataSet qds = handler.getDataSet();
        QubeDataSetIterator it = new QubeDataSetIterator(qds);
        while (it.hasNext()) {
            it.next();
            System.err.println(" " + it + " " + it.getValue(qds));
        }
        QDataSet tds = (QDataSet) qds.property(QDataSet.DEPEND_0);
        it = new QubeDataSetIterator(tds);
        while (it.hasNext()) {
            it.next();
            System.err.println(" " + it + " " + it.getValue(tds));
        }
        return qds;
    }
