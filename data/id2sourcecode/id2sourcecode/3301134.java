    public String uploadPost(String textToUpload, String nameAndTitle, String email, String subdomain, boolean isPrivate, String expirydate, String textFormatType) {
        try {
            this.setPaste_code_data(textToUpload);
            this.setPaste_name_data(nameAndTitle);
            this.setPaste_email_data(email);
            this.setPaste_subdomain_data(subdomain);
            if (isPrivate) {
                this.setPaste_private_data("1");
            } else {
                this.setPaste_private_data("0");
            }
            this.setPaste_expire_date_data(expirydate);
            this.setPaste_format_data(textFormatType);
            loadParams(this.paste_code_key, this.getPaste_code_data());
            if (this.getPaste_name_data() != null) {
                loadParams(this.paste_name_key, this.getPaste_name_data());
            }
            if (this.getPaste_email_data() != null) {
                loadParams(this.paste_email_key, this.getPaste_email_data());
            }
            if (this.getPaste_subdomain_data() != null) {
                loadParams(this.paste_subdomain_key, this.getPaste_subdomain_data());
            }
            loadParams(this.paste_private_key, this.getPaste_private_data());
            if (this.getPaste_expire_date_data() != null) {
                loadParams(this.paste_expire_date_key, this.getPaste_expire_date_data());
            }
            if (this.getPaste_format_data() != null) {
                loadParams(this.paste_format_key, this.getPaste_format_data());
            }
            URL url = new URL("http://pastebin.com/api_public.php");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter writeOut = new OutputStreamWriter(conn.getOutputStream());
            writeOut.write(getParamData());
            writeOut.flush();
            BufferedReader readIn = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = readIn.readLine();
            writeOut.close();
            readIn.close();
            return line;
        } catch (IOException ex) {
            Logger.getLogger(PostData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
