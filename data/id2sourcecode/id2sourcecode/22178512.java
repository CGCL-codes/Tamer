        private void postToYapbam(Message message) throws IOException {
            StringWriter writer = new StringWriter();
            message.error.printStackTrace(new PrintWriter(writer));
            String trace = writer.getBuffer().toString();
            StringBuilder data = new StringBuilder();
            data.append(URLEncoder.encode("error", ENC)).append("=").append(URLEncoder.encode(trace, ENC));
            addToBuffer(data, "country", message.country);
            addToBuffer(data, "javaVendor", message.javaVendor);
            addToBuffer(data, "javaVersion", message.javaVersion);
            addToBuffer(data, "lang", message.lang);
            addToBuffer(data, "osName", message.osName);
            addToBuffer(data, "osVersion", message.osVersion);
            addToBuffer(data, "version", message.version);
            URL url = new URL("http://www.yapbam.net/crashReport.php");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), ENC);
            try {
                wr.write(data.toString());
                wr.flush();
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), ENC));
                try {
                    for (String line = rd.readLine(); line != null; line = rd.readLine()) {
                        System.out.println(line);
                    }
                } finally {
                    rd.close();
                }
            } finally {
                wr.close();
            }
        }
