    @Deprecated
    public static void main(String[] args) throws Exception {
        SDCrossValidator cv = new SDCrossValidator("en");
        cv.evaluate(new SentenceSampleStream(new PlainTextByLineStream(new FileInputStream("/home/joern/Infopaq/opennlp.data/en/eos/eos.all").getChannel(), "ISO-8859-1")), 10);
        System.out.println(cv.getFMeasure().toString());
    }
