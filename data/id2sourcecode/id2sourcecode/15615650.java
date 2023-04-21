    @SuppressWarnings("deprecation")
    public void main(String[] str) {
        URL url = null;
        Scanner sc = null;
        String apiurl = "http://www.deanclatworthy.com/imdb/";
        String moviename = null;
        String dataurl = null;
        String retdata = null;
        InputStream is = null;
        DataInputStream dis = null;
        try {
            sc = new Scanner(System.in);
            moviename = sc.nextLine();
            if (moviename == null || moviename.equals("")) {
                System.out.println("No movie found");
                System.exit(1);
            }
            moviename = moviename.trim();
            moviename = moviename.replace(" ", "+");
            dataurl = apiurl + "?q=" + moviename + "&type=text";
            System.out.println("Getting data from service");
            System.out.println("########################################");
            url = new URL(dataurl);
            is = url.openStream();
            dis = new DataInputStream(is);
            String details[];
            while ((retdata = dis.readLine()) != null) {
                if (retdata.equals("error|Film not found")) {
                    System.out.println("No such movie found");
                    break;
                }
                retdata = retdata.replace("|", "#");
                details = retdata.split("#");
                System.out.print(details[0].toUpperCase() + " -> ");
                System.out.print(details[1]);
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                if (dis != null) {
                    dis.close();
                }
                if (is != null) {
                    is.close();
                }
                if (sc != null) {
                    sc.close();
                }
            } catch (Exception e2) {
                ;
            }
        }
    }
