    public static void extractInfo(String data, String fileName) {
        try {
            URL url;
            url = new URL(data);
            URLConnection urlconn = url.openConnection();
            urlconn.setDoInput(true);
            System.out.println("\nSuccessful");
            BufferedReader in = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
            StringBuffer output = new StringBuffer();
            String temp;
            while ((temp = in.readLine()) != null) {
                System.out.println(temp);
                output.append(temp);
            }
            in.close();
            FileWriter fstream = new FileWriter(fileName);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(output.toString());
            out.close();
        } catch (MalformedURLException e2) {
            System.out.println("MalformedURlException: " + e2);
        } catch (IOException e3) {
            System.out.println("IOException: " + e3);
        }
    }
