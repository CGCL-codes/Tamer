    public static boolean updateCheck() throws Exception {
        String v = Version.getVersion().getVersionId();
        v = v.replaceAll("\\.", "_");
        URL url = new URL(UIDefaults.API_URL + "version/" + v);
        BufferedReader in = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int status = conn.getResponseCode();
            if (status == 200) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder str = new StringBuilder();
                while (true) {
                    String line = in.readLine();
                    if (line == null) break;
                    str.append(line);
                }
                System.out.println(str);
                JAXBContext jc = JAXBContext.newInstance("com.ifreebudget.web.service.response");
                Unmarshaller um = jc.createUnmarshaller();
                JAXBElement<UpdateCheckResponse> elem = (JAXBElement<UpdateCheckResponse>) um.unmarshal(new StringReader(str.toString()));
                UpdateCheckResponse resp = elem.getValue();
                if (resp.isUpdateRequired()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                throw new RuntimeException(tr("Unable to get latest version, server unavailable."));
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
