    public static String fetch(URL a_url) throws IOException {
        BufferedReader dis = null;
        try {
            URLConnection uc = a_url.openConnection();
            StringBuffer sb = new StringBuffer();
            dis = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            while (true) {
                String s = dis.readLine();
                if (s == null) break;
                sb.append(s + "\n");
            }
            return sb.toString();
        } catch (IOException e) {
            System.out.println("Unable to fetch " + a_url + "(IOexception)");
            return "no document";
        } finally {
            if (dis != null) dis.close();
        }
    }
