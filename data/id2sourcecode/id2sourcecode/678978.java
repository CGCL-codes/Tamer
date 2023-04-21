    public static void main(String[] args) throws IOException {
        String myurl = new String("http://localhost:8080/Discuz/bbs/forumdisplay.php?fid=12");
        int i = 0;
        URL url = new URL(myurl);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String s = "";
        StringBuffer sb = new StringBuffer("");
        while ((s = br.readLine()) != null) {
            i++;
            sb.append(s + "\r\n");
        }
        System.out.println("i=" + i + "\n");
        BufferedWriter writer = null;
        writer = new BufferedWriter(new FileWriter("..\\a.html"));
        writer.write(sb.toString());
        writer.close();
    }
