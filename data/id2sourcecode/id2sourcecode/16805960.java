    public static String[] informacionPersona(String cedula) {
        String[] datos = null;
        try {
            URL url = new URL("http://altamar17.5gbfree.com/pruebas/post2.php?cedula=" + cedula);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String nombre;
            while ((line = rd.readLine()) != null) {
                int inicio = line.indexOf("Señor(a)");
                int fin = line.indexOf("identificado(a)");
                if (line.contains("Señor(a) &nbs")) {
                    nombre = line;
                    nombre = nombre.replace("/", "Ñ");
                    System.out.println(nombre.substring(inicio + 16, fin));
                    datos = nombre.substring(inicio + 16, fin).split(" ");
                }
            }
            rd.close();
        } catch (Exception e) {
        }
        return datos;
    }
