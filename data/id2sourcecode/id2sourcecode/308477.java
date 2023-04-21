    public static void main(String[] args) {
        tilt t;
        if (args.length == 0) t = new tilt("iw-room2", "COM3"); else if (args.length == 1) t = new tilt(args[0], "COM3"); else t = new tilt(args[0], args[1]);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                t.getOutputStream().write(in.readLine().getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
