            public void run() {
                try {
                    URL url = new URL(url_string);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    while (true) {
                        int character = inputStream.read();
                        if (character == -1) break;
                        usageTimesAll = usageTimesAll + (char) character;
                    }
                    if (usageTimesAll.indexOf("Access denied.") != -1) {
                        usageTimesAll = "";
                    } else {
                        String start = "data: <b>";
                        String end = "</b><br>Records: ";
                        int indexStart = usageTimesAll.indexOf(start) + start.length();
                        int indexEnd = usageTimesAll.indexOf(end);
                        usageTimesAll = usageTimesAll.substring(indexStart, indexEnd);
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    System.out.println("Error: Can't connect to Rachota Analytics server.");
                    usageTimesAll = null;
                }
            }
