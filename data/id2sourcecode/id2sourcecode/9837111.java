    @Test
    public void test4() {
        try {
            String resource = "before/test4.json";
            InputStream is = getClass().getClassLoader().getResourceAsStream(resource);
            Reader reader = new InputStreamReader(is);
            StringWriter writer = new StringWriter();
            FileUtilities.copy(reader, writer);
            String before = writer.toString();
            resource = "after/test4.json";
            is = getClass().getClassLoader().getResourceAsStream(resource);
            reader = new InputStreamReader(is);
            writer = new StringWriter();
            FileUtilities.copy(reader, writer);
            String answer = writer.toString();
            Beautifier beautifier = new JsonBeautifier();
            beautifier.setEditMode("json");
            beautifier.setLineSeparator("\n");
            beautifier.setTabWidth(4);
            beautifier.setIndentWidth(4);
            beautifier.setUseSoftTabs(true);
            beautifier.setWrapMargin(80);
            beautifier.setWrapMode("none");
            String after = beautifier.beautify(before);
            assertTrue("json test 4 failed.", answer.equals(after));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
