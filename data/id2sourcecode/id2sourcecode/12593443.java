    public void Download() {
        try {
            URL url = new URL("http://www.optymo.fr/ws/calcul.aspx");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            String data = "arret_dep=";
            data += Depart;
            data += "&type_dep=arret";
            data += "&arret_arr=";
            data += Arrivee;
            data += "&type_arr=arret";
            data += "&heureDep=";
            data += Hour;
            data += "%3A";
            data += Minute;
            data += "&dateDep=";
            data += Day;
            data += "-";
            data += Month;
            data += "-";
            data += Year;
            data += "&hidden_arret_dep=";
            data += Depart;
            data += "&hidden_type_dep=arret";
            data += "&hidden_arret_arr=";
            data += Arrivee;
            data += "&hidden_type_arr=arret";
            data += "&envois_id_dep=aid_";
            data += Depart;
            data += "&envois_id_arr=aid_";
            data += Arrivee;
            data += "&envois_heure_dep=";
            data += (Hour * 60) + Minute;
            data += "&envois_date_dep=";
            data += Year;
            data += "-";
            data += Month;
            data += "-";
            data += Day;
            data += "&Submit=+";
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data + "\r\n\r\n");
            wr.flush();
            wr.close();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            Pattern p = Pattern.compile("DataXml'\\)\\.value='(.*)';");
            boolean done = false;
            while ((line = rd.readLine()) != null && done != true) {
                Matcher m = p.matcher(line);
                while (m.find()) {
                    FileOutputStream fichier = context.openFileOutput("tempiti.xml", 0);
                    OutputStreamWriter osw = new OutputStreamWriter(fichier);
                    osw.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
                    osw.write(m.group(1));
                    osw.close();
                    done = true;
                }
            }
            rd.close();
        } catch (Exception e) {
            System.out.println("Erreur : Impossible de t�l�charger les r�sultats de l'itin�raire.");
        }
    }
