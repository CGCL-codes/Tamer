    public StaticSentence(Session s, String sentence, SerializerWrite serwrite, SerializerRead serread) {
        this(s, new NormalBuilder(sentence), serwrite, serread);
    }
