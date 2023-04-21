    public void commandLineInterface() {
        String query = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(System.in, Constants.DEFAULT_CHARSET));
            System.out.print("URL2Docno >");
            while ((query = reader.readLine()) != null) {
                if (query.equals("")) {
                    continue;
                }
                if (query.equalsIgnoreCase("quit") || query.equalsIgnoreCase("exit")) {
                    break;
                }
                System.out.println("***************************************");
                System.out.println(url2Docno(MD5.digest(query)));
                System.out.println("***************************************");
                System.out.print("URL2Docno >");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
