    public boolean submit_list(String gl_name, String gl_desc, String gl_list) {
        try {
            String data = URLEncoder.encode("PHPSESSID", "UTF-8") + "=" + URLEncoder.encode(this.get_session(), "UTF-8");
            data += "&" + URLEncoder.encode("gl_name", "UTF-8") + "=" + URLEncoder.encode(gl_name, "UTF-8");
            data += "&" + URLEncoder.encode("gl_desc", "UTF-8") + "=" + URLEncoder.encode(gl_desc, "UTF-8");
            data += "&" + URLEncoder.encode("genome_id", "UTF-8") + "=" + URLEncoder.encode("9606", "UTF-8");
            data += "&" + URLEncoder.encode("gene_id_type", "UTF-8") + "=" + URLEncoder.encode("2", "UTF-8");
            data += "&" + URLEncoder.encode("gl_list", "UTF-8") + "=" + URLEncoder.encode(gl_list, "UTF-8");
            System.out.println("URL: " + URL_LOLA + FILE_CONFIRM_LIST);
            URL url = new URL(URL_LOLA + FILE_CONFIRM_LIST);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            wr.close();
            rd.close();
        } catch (Exception e) {
            System.out.println("error in list submission");
            e.printStackTrace();
        }
        return true;
    }
