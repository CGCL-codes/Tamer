    public Definition getDefinition(String lojban_word) throws IOException {
        try {
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
                    boolean rafsi_block_started = false;
                    String definition = "";
                    String rafsi = "";
                    while ((line = reader.readLine()) != null) {
                        if (line.contains(WORD_NOT_FOUND_TAG)) {
                            return null;
                        }
                        if (rafsi_block_started) {
                            rafsi_block_started = false;
                            rafsi = line;
                        }
                        if (line.contains(RAFSI_TAG)) {
                            rafsi_block_started = true;
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
                            break;
                        }
                    }
                    if (!def_started) {
                        if (tries < max_tries) {
                            tries += 1;
                            continue;
                        } else {
                            return null;
                        }
                    }
                    definition = cleanDefinition(definition);
                    rafsi = cleanType(rafsi);
                    Definition def = new Definition(definition);
                    def.setRafsi(rafsi);
                    return def;
                } catch (SocketTimeoutException e) {
                    tries += 1;
                } catch (Exception e) {
                    tries += 1;
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
            return null;
        } finally {
            try {
                final int _1_second = 1000;
                Thread.sleep(_1_second);
            } catch (InterruptedException e) {
            }
        }
    }
