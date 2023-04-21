    public String agregarUsuario(String nombre, String apellido, String email, String usuario, String contrasena) {
        String responce = "";
        String request = conf.Conf.agregarUsuario;
        OutputStreamWriter wr = null;
        BufferedReader rd = null;
        try {
            URL url = new URL(request);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write("nUsuario=" + usuario + "&contrasena=" + contrasena + "&nombre=" + nombre + "&apellido=" + apellido + "&email=" + email);
            wr.flush();
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                responce += line;
            }
        } catch (Exception e) {
        }
        return responce;
    }
