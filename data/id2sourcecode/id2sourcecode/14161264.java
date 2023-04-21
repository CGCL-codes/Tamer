    public static void main(String arg[]) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("enter userName:");
            String username = sc.next();
            System.out.println();
            System.out.print("enter pass:");
            String password = sc.next();
            System.out.println();
            url = url + "username=" + username + "&password=" + password;
            URL urlCon = new URL(url);
            URLConnection conn = urlCon.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            File f = new File("Login.txt");
            BufferedWriter bwr = new BufferedWriter(new FileWriter(f));
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
                bwr.write(line + "\n");
            }
            rd.close();
            bwr.close();
            IBMDigester.parse("Login.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
