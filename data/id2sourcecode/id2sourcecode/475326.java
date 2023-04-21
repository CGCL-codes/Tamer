    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 36);
        InputStream in = socket.getInputStream();
        PrintStream out = new PrintStream(socket.getOutputStream());
        int result;
        for (int i = 0; i < args.length; i++) {
            if (i > 0) out.print(" ");
            out.print(args[i]);
        }
        out.println();
        while ((result = in.read()) >= 0) System.out.write(result);
    }
