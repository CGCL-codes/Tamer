    public void testWriteObjectMethod() {
        SourceGenerator gen = new SourceGenerator();
        StringBuffer buf = new StringBuffer();
        gen.writeObjectMethod(buf, JReleaseInfoProperty.TYPE_PRI_INT, "number", "33");
        gen.writeObjectMethod(buf, JReleaseInfoProperty.TYPE_PRI_BOOLEAN, "ready", "true");
        gen.writeObjectMethod(buf, JReleaseInfoProperty.TYPE_OBJ_STRING, "name", "Name");
        gen.writeObjectMethod(buf, JReleaseInfoProperty.TYPE_OBJ_BOOLEAN, "done", "false");
        gen.writeObjectMethod(buf, JReleaseInfoProperty.TYPE_OBJ_INTEGER, "count", "33");
        gen.writeDateMethod(buf, JReleaseInfoProperty.TYPE_OBJ_DATE, "build", new Date());
        System.out.println(buf.toString());
    }
