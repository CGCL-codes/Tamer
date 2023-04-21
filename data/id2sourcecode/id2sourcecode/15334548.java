    public static void sendErrorEmail(String urlString, String programName, String programVersion, Exception ex) {
        class Versenden extends Thread {

            private String urlString;

            private String programName;

            private String programVersion;

            private Exception ex;

            public Versenden(String urlString, String programName, String programVersion, Exception ex) {
                this.ex = ex;
                this.programName = programName;
                this.programVersion = programVersion;
                this.urlString = urlString;
            }

            public void run() {
                try {
                    String stacktrace;
                    {
                        StringBuffer buff = new StringBuffer();
                        buff.append(ex.toString());
                        buff.append("\n\n");
                        StackTraceElement[] elem = ex.getStackTrace();
                        for (int i = 0; i < elem.length; i++) {
                            buff.append(elem[i]);
                            buff.append("\n");
                        }
                        stacktrace = buff.toString();
                    }
                    String data = URLEncoder.encode("version", "UTF-8") + "=" + URLEncoder.encode(programVersion, "UTF-8");
                    data += "&" + URLEncoder.encode("program", "UTF-8") + "=" + URLEncoder.encode(programName, "UTF-8");
                    data += "&" + URLEncoder.encode("stacktrace", "UTF-8") + "=" + URLEncoder.encode(stacktrace, "UTF-8");
                    if (ex.getMessage() != null) {
                        data += "&" + URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(ex.getMessage(), "UTF-8");
                    }
                    URL url = new URL(urlString);
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(data);
                    wr.flush();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = rd.readLine()) != null) {
                        System.out.println(line);
                    }
                    wr.close();
                    rd.close();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        new Versenden(urlString, programName, programVersion, ex).start();
    }
