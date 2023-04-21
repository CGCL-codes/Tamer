    public static void refresh() throws IOException {
        final URL url = new URL("http://127.0.0.1:9000/rpc/rescan");
        final URLConnection con = url.openConnection();
        final StringBuilder sb = new StringBuilder();
        final BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        System.out.println(sb);
    }
