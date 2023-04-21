    public static Film recupFilm(String idmc) throws ConnectionException {
        try {
            URL url = new URL("http://www.moviecovers.com/getfilm.html");
            URLConnection connect = url.openConnection();
            connect.setDoOutput(true);
            connect.connect();
            PrintWriter os = new PrintWriter(connect.getOutputStream());
            os.print("idmc=" + encode(toIDMC(idmc)));
            os.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            Film f = new Film(br);
            try {
                recupImage(idmc, f.getTitle());
            } catch (ConnectionException ce) {
            }
            return f;
        } catch (Exception e) {
            throw new ConnectionException("Impossible d'�tablir la connection � Moviecovers.com");
        }
    }
