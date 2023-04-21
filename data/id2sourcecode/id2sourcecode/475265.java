    void sendViaPOST(URL url) {
        try {
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            Iterator i = fields.iterator();
            String data = null;
            if (i.hasNext()) {
                FormField firstfield = (FormField) i.next();
                data = new String();
                data = URLEncoder.encode(firstfield.name, "UTF-8") + "=" + URLEncoder.encode(firstfield.flood_value, "UTF-8");
            }
            while (i.hasNext()) {
                FormField field = (FormField) i.next();
                if (field.name == null || field.flood_value == null) {
                    System.out.println("Null van itt:" + field.name);
                }
                data += "&" + URLEncoder.encode(field.name, "UTF-8") + "=" + URLEncoder.encode(field.flood_value, "UTF-8");
            }
            if (data != null) {
                System.out.println(data);
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(data);
                wr.flush();
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    System.out.println("Response:" + line);
                }
                wr.close();
                rd.close();
            }
        } catch (IOException ioe) {
            System.out.println("Cannot write to URL!");
            return;
        }
    }
