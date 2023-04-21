    public static String sendGetRequest(String endpoint, String requestParameters) {
        String result = null;
        if (endpoint.startsWith("http://")) {
            try {
                StringBuffer data = new StringBuffer();
                String urlStr = endpoint;
                if (requestParameters != null && requestParameters.length() > 0) {
                    urlStr += "?" + requestParameters;
                }
                URL url = new URL(urlStr);
                URLConnection conn = url.openConnection();
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String line, interesante = "";
                String[] datos = {};
                while ((line = rd.readLine()) != null) {
                    if (line.contains("intente") && line.contains("Ocupado")) {
                        estaOcupado = true;
                        break;
                    }
                    if (line.contains("Suspension") && line.contains("Derechos")) {
                        estaSuspendido = true;
                        break;
                    }
                    if (line.contains("Muerte")) {
                        estaMuerto = true;
                        break;
                    }
                    if (line.contains("No se encuentra en el censo ")) {
                        noEstaEnElCenso = true;
                    }
                    if (!line.isEmpty() && line.contains("<td")) {
                        interesante += sinAtributos(line);
                    }
                    sb.append(line + "\n");
                }
                datos = interesante.split("</td>");
                datosFinales = datosDelZonificado(datos);
                rd.close();
                result = sb.toString();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Verifique su conexion a internet");
            }
        }
        return result;
    }
