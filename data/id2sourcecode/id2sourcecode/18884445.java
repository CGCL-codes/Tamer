    public String getDefinition(String lojban_word) throws IOException {
        int tries = 0;
        while (tries < max_tries) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(encodeUrl(url_string + lojban_word));
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                boolean english_block_started = false;
                boolean def_started = false;
                String definition = "";
                while ((line = reader.readLine()) != null) {
                    if (line.contains(WORD_NOT_FOUND_TAG)) {
                        return null;
                    }
                    if (line.contains(ENGLISH_BLOCK_START_TAG)) {
                        english_block_started = true;
                    }
                    if (english_block_started && line.contains(DEF_START_TAG)) {
                        def_started = true;
                    }
                    if (def_started) {
                        definition += line;
                    }
                    if (def_started && line.contains(DEF_END_TAG)) {
                        System.out.println("\t------->Whole definition found!");
                        break;
                    }
                }
                if (!def_started) {
                    if (tries < max_tries) {
                        tries += 1;
                        System.out.println("Ran through whole web page, could not find definition for [" + lojban_word + "], we have tried [" + tries + "] time(s)");
                        continue;
                    } else {
                        return DEFINITION_NOT_FOUND;
                    }
                }
                int def_tag_start = definition.indexOf(DEF_START_TAG);
                definition = definition.substring(def_tag_start + DEF_START_TAG.length());
                String table_data_open = "<td>";
                int next_table_data_open_start = definition.indexOf(table_data_open);
                definition = definition.substring(next_table_data_open_start + table_data_open.length());
                definition = definition.substring(0, definition.indexOf("</td>"));
                definition = definition.replaceAll("<sub>", "");
                definition = definition.replaceAll("</sub>", "");
                return definition;
            } catch (SocketTimeoutException e) {
                tries += 1;
                System.out.println("Got socket timeout for [" + lojban_word + "], we have tried [" + tries + "] time(s)");
            } catch (Exception e) {
                tries += 1;
                System.out.println("-------------------------------------------------");
                e.printStackTrace();
                System.out.println("-------------------------------------------------");
                System.out.println("Trying Jbovlaste again for [" + lojban_word + "], we have tried [" + tries + "] time(s)");
                System.out.println("-------------------------------------------------");
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        return DEFINITION_NOT_FOUND;
    }
