            @Override
            public void run() {
                if (MISSING_KEYS.size() > 0) {
                    try {
                        System.out.println("Sending missing phrases to author...:");
                        String message = "Stop Motion Capture:\nFollowing phrases do nott exist: \n";
                        for (String key : MISSING_KEYS) {
                            message += key + "\n";
                            System.out.println(" - \"" + key + "\"");
                        }
                        URL url = new URL("http://mafrasi.sistemich.de/java/sendMail.php");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        PrintStream ps = new PrintStream(connection.getOutputStream());
                        ps.write(("info=" + java.net.URLEncoder.encode(message, "UTF-8")).getBytes());
                        ps.flush();
                        String r = new java.util.Scanner(connection.getInputStream()).useDelimiter("\\Z").next();
                        System.out.println("Server answer: " + r);
                        ps.close();
                        connection.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
