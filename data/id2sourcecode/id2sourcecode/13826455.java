    private String extractPageRevisionsFromWiki(final String pageName) {
        Integer numberOfRevisions = 0;
        final HashSet<String> hsUsers = new HashSet<String>();
        try {
            String data = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode("Special:Export", "UTF-8");
            data += "&" + URLEncoder.encode("pages", "UTF-8") + "=" + URLEncoder.encode(pageName, "UTF-8");
            data += "&" + URLEncoder.encode("history", "UTF-8") + "=" + URLEncoder.encode("true", "UTF-8");
            final URL url = new URL(HOST_REV);
            final URLConnection urlConn = url.openConnection();
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            final OutputStreamWriter urlWriter = new OutputStreamWriter(urlConn.getOutputStream());
            urlWriter.write(data);
            urlWriter.flush();
            final BufferedReader urlReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            final File outputFile = new File(DESTINATION_FOLDER + pageName + ".xml");
            outputFile.createNewFile();
            final FileWriter outputFileWriter = new FileWriter(outputFile);
            String line;
            boolean nextIsAuthor = false;
            while ((line = urlReader.readLine()) != null) {
                outputFileWriter.write(line + "\n");
                if (line.contains("<revision>")) {
                    numberOfRevisions++;
                }
                if (nextIsAuthor) {
                    nextIsAuthor = false;
                    if (line.contains("<ip>") && line.contains("</ip")) {
                        hsUsers.add(line);
                    }
                }
                if (line.contains("<contributor>")) {
                    nextIsAuthor = true;
                }
            }
            outputFileWriter.flush();
            urlWriter.close();
            urlReader.close();
            outputFileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return numberOfRevisions + " revisions, " + hsUsers.size() + " users";
    }
