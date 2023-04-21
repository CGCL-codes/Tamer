    @BeforeClass
    public static void generateTestData() throws Exception {
        FileInputStream messagesStream = new FileInputStream(messages);
        FileInputStream setsStream = new FileInputStream(sets);
        FileInputStream compositesStream = new FileInputStream(composites);
        FileInputStream fieldsStream = new FileInputStream(fields);
        ByteArrayOutputStream messageOutStream = new ByteArrayOutputStream();
        ByteArrayOutputStream setsOutStream = new ByteArrayOutputStream();
        ByteArrayOutputStream compositeOutStream = new ByteArrayOutputStream();
        ByteArrayOutputStream fieldsOutStream = new ByteArrayOutputStream();
        FileUtil.copy(messagesStream, messageOutStream);
        FileUtil.copy(setsStream, setsOutStream);
        FileUtil.copy(compositesStream, compositeOutStream);
        FileUtil.copy(fieldsStream, fieldsOutStream);
        schema = testCandidate.readSchema(messageOutStream.toByteArray(), setsOutStream.toByteArray(), compositeOutStream.toByteArray(), fieldsOutStream.toByteArray());
    }
