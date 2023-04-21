    public static void main(String[] args) throws Exception {
        URL target = new URL("http://localhost:8081/mfw/bm/os_version");
        HttpURLConnection url = (HttpURLConnection) target.openConnection();
        url.setDoInput(true);
        url.setDoOutput(true);
        url.setUseCaches(false);
        url.setRequestProperty("Content-Type", "text/plain");
        url.setRequestMethod("POST");
        DataOutputStream printout = new DataOutputStream(url.getOutputStream());
        String content = "A value has been observed.";
        printout.writeBytes(content);
        printout.flush();
        printout.close();
        BufferedReader br = new BufferedReader(new InputStreamReader(url.getInputStream()));
        String str;
        while (null != ((str = br.readLine()))) {
            System.out.println(str);
        }
        br.close();
    }
