    private static void redirectProcToOut(final Process _proc) {
        BufferedInputStream in = new BufferedInputStream(_proc.getInputStream());
        final BufferedReader br = new BufferedReader(new InputStreamReader(in));
        try {
            System.out.write(br.readLine().getBytes());
            String s;
            while ((s = br.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
