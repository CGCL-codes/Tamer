    protected boolean emailRecover(String userIn, String emailIn) {
        try {
            if (server.VERBOSE) {
                System.out.println("RECOVERING INFO FROM: " + PICAIUServer.MYHOST);
            }
            URL url = new URL(PICAIUServer.MYHOST + "recover.php?user=" + userIn + "&server=PICAIU" + "&email=" + emailIn + "&ver=" + verificationCode + "&authPass=" + server.getAuthPass());
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            String status = sb.toString();
            System.out.println("Email Recover Status=" + status);
            if (status.equals("recovered correctly!")) {
                return true;
            }
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
