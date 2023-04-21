    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = new ContextStack();
        List<String> randomStrings = new ArrayList<String>();
        Random random = new Random();
        for (int i = 0, num = random.nextInt(100); i < num; ++i) {
            int minChar = 32, maxChar = 126;
            int stringSize = random.nextInt(30);
            StringBuilder buf = new StringBuilder();
            for (int j = 0; j < stringSize; ++j) {
                int charId = minChar + random.nextInt(maxChar - minChar);
                buf.append((char) charId);
            }
            randomStrings.add(buf.toString());
        }
        context.put("name", "Tom");
        context.put("randomStrings", randomStrings);
        context.put("intArray", intArray);
        context.put("intList", TemplateUtils.objectToCollection(intArray));
        context.put("doubleArray", doubleArray);
        context.put("doubleList", TemplateUtils.objectToCollection(doubleArray));
    }
