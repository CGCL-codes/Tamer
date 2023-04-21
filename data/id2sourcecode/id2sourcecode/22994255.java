    public static void main(String[] args) throws Exception {
        JatherClient slowClient = new JatherClient("slow");
        JatherClient fastClient = new JatherClient("fast");
        List<Future<String>> calls = new ArrayList<Future<String>>();
        for (int i = 0; i < 5; i++) {
            calls.add(slowClient.submit(new MyCallable(1000)));
            calls.add(fastClient.submit(new MyCallable(100000)));
        }
        for (Future<String> f : calls) {
            System.out.println(f.get());
        }
        System.out.println(slowClient.getChannel().getProperties());
        slowClient.close();
        fastClient.close();
    }
