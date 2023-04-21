    public static void main(String[] args) throws Throwable {
        String location = "localhost:6666";
        if (args.length >= 1) {
            if (args.length >= 2) {
                location = args[1];
            }
            BlobClient client = new BlobClient(location);
            if (args[0].equals("read")) {
                client.read(System.in, System.out);
                return;
            } else if (args[0].equals("get")) {
                client.get(System.in, System.out);
                return;
            } else if (args[0].equals("write")) {
                client.write(System.in, System.out);
                return;
            } else if (args[0].equals("post")) {
                client.post(System.in, System.out);
                return;
            }
            throw new Exception("Specify command: read,get or write,post");
        }
    }
