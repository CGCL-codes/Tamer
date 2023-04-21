    @Test
    public void testSimpToTradReader() throws IOException {
        String simpStr = "������ԡ��˹���������ͺ˷�Ӧ�ѣ�";
        String tradStr = "�����Z�ԡ��˹��ǻ�ܛ�w�ͺ˷����t��";
        Reader reader = new StringReader(simpStr);
        Writer writer = new StringWriter();
        ChineseUtils.simpToTrad(reader, writer, true);
        System.out.println(writer.toString());
        assertTrue(tradStr.equals(writer.toString()));
    }
