    @Test
    public void testTradToSimpReader() throws IOException {
        String simpStr = "������ԡ��˹���������ͺ˷�Ӧ�ѣ�";
        String tradStr = "�����Z�ԡ��˹��ǻ�ܛ�w�ͺ˷����t��";
        Reader reader = new StringReader(tradStr);
        Writer writer = new StringWriter();
        ChineseUtils.tradToSimp(reader, writer, true);
        System.out.println(writer.toString());
        assertTrue(simpStr.equals(writer.toString()));
    }
