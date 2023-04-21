    public static void main(String[] args) {
        short i = 10;
        System.out.println(Integer.MAX_VALUE);
        NativeTreeStore store = (NativeTreeStore) ctx.getBean("nativeTreeStore");
        ContentStore contentStore = (ContentStore) ctx.getBean("nativeContentStore");
        ContentWriter writer = contentStore.getContentWriter();
        writer.putContent("YAHOO");
        System.out.println(writer.getUri());
        System.out.println(writer.getUri().length());
        ContentReader reader = contentStore.getContentReader(writer.getUri());
        System.out.println(reader.getContentString());
    }
