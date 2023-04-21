    public void setStatusVideoKonvertierung(int statusVideoKonvertierung) {
        DatabaseConnection database = Datenspeicher.getDatabase();
        if (!database.isConnected()) database.connect();
        if (statusVideoKonvertierung == 0) {
            database.updateStatus(readActionId(0, 0), 200, "Videokonvertierung problemlos beendet");
        }
        String text = "";
        try {
            URL url = new URL("http://localhost:8080/virtPresenterVerwalter/videofile.jsp?id=" + this.getId());
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
            String zeile = "";
            while ((zeile = in.readLine()) != null) {
                text += zeile + "\n";
            }
            in.close();
            http.disconnect();
            saveVideoXMLOnWebserver(text);
            database.statusSpeichern(id, 210, 0, "Auftrag beendet");
            saveLecturerecordingsXMLOnWebserver();
            if (seminar == null) seminar = Seminar.readSeminar(seminarID);
            if (seminar != null && seminar.getStandardFreigabe() != 0) {
                saveFreigabe(seminar.getStandardFreigabe(), "system");
                if (seminar.getStandardFreigabe() != 3 && seminar.getStandardFreigabe() != 4) {
                    if (Datenspeicher.getVaderConnect().jobIsInVader(id)) Datenspeicher.getVaderConnect().veranstaltungAendern(this); else Datenspeicher.getVaderConnect().veranstaltungEintragen(this);
                } else if (Datenspeicher.getVaderConnect().jobIsInVader(id)) Datenspeicher.getVaderConnect().veranstaltungLoeschen(id);
            }
            System.err.println("Job " + this.getId() + " erfolgreich bearbeitet!");
        } catch (MalformedURLException e) {
            System.err.println("Job " + this.getId() + ": Konnte video.xml oder lecturerecordings.xml nicht erstellen. Verbindung konnte nicht aufgebaut werden.");
        } catch (IOException e) {
            System.err.println("Job " + this.getId() + ": Konnte video.xml oder lecturerecordings.xml nicht erstellen. Konnte Daten nicht lesen/schreiben.");
        }
    }
