    public String readURL(String fileName) {
        String text = "";
        URL url;
        try {
            url = new URL(getCodeBase(), "fdFiles/" + fileName);
            try {
                URLConnection con = url.openConnection();
                String inputLine = "";
                StringBuffer buf = new StringBuffer("");
                DataInputStream in = new DataInputStream(new BufferedInputStream(con.getInputStream()));
                while ((inputLine = in.readLine()) != null) {
                    buf.append(inputLine + "\n");
                }
                text = buf.toString();
            } catch (IOException erre) {
                printSign("Error loading.");
            }
        } catch (MalformedURLException er) {
        }
        return text;
    }
