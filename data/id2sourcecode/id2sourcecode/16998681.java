    public static void main(String[] args) throws URISyntaxException, IOException, NumberFormatException, SQLException {
        String[] lettres = new String[] { "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y" };
        for (int i = 0; i < lettres.length; i++) {
            URL fileURL = new URL("http://www.metal-archives.com/browseL.php?l=" + lettres[i]);
            URLConnection urlConnection = fileURL.openConnection();
            InputStream httpStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(httpStream));
            String ligne = null;
            boolean b = true;
            int li;
            int li2;
            String href;
            int nbGroupes = 0;
            MetalArchivesDAO mag = MetalArchivesDAOImpl.getInstance();
            while ((ligne = br.readLine()) != null && b) {
                if (ligne.indexOf("<a href='band.php?id=") != -1) {
                    StringBuffer sb = new StringBuffer(ligne.substring(ligne.indexOf("<a href='band.php?id=")));
                    li = 0;
                    li2 = sb.indexOf("</a><br>", li);
                    while (li2 != -1) {
                        href = sb.substring(li, li2);
                        nbGroupes++;
                        li = li2 + 8;
                        li2 = sb.indexOf("</a><br>", li);
                    }
                }
            }
            br.close();
            httpStream.close();
            log.info("Nombre de groupe inseres [" + lettres[i] + "] = " + nbGroupes);
        }
    }
