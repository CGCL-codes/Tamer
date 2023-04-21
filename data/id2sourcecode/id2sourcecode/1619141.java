    public String httpGet(String urlAddress) throws IOException {
        URL url = new URL(urlAddress);
        InputStream in = null;
        StringBuilder sb = new StringBuilder();
        try {
            URLConnection c = url.openConnection();
            in = c.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
