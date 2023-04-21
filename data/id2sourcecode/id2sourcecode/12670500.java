    public static boolean uploadSong(String username, String playlistName, Song song, FileInputStream resource) {
        String method_Name = "createASongInfo";
        String link = "";
        String soap_Action = NAMESPACE + ":" + method_Name;
        SoapObject request = new SoapObject(NAMESPACE, method_Name);
        request.addProperty("usernname", username);
        request.addProperty("playlistName", playlistName);
        PropertyInfo info = new PropertyInfo();
        info.setName("song");
        info.setValue(song);
        info.setType(song.getClass());
        request.addProperty(info);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        AndroidHttpTransport tran = new AndroidHttpTransport(URL);
        try {
            tran.call(soap_Action, envelope);
            link = envelope.getResponse().toString();
            System.out.println(link);
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            BufferedInputStream inp = new BufferedInputStream(resource);
            BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream());
            byte[] array = new byte[SIZE];
            int read;
            while ((read = inp.read(array)) != -1) {
                out.write(array, 0, read);
            }
            inp.close();
            out.close();
            connection.getResponseCode();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
