    private void copyConfFromStandard() {
        BufferedWriter writeStream = null;
        BufferedReader stdStream = null;
        try {
            String readLine;
            confFile.createNewFile();
            stdStream = new BufferedReader(new InputStreamReader(ConfigurationParser.class.getResourceAsStream(DEFAULT_CONFIG_PATH)));
            writeStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(confFile)));
            readLine = stdStream.readLine();
            while (readLine != null) {
                if (readLine.length() > 0 && readLine.charAt(0) != COMMENT_CHAR) {
                    writeStream.write(readLine);
                    writeStream.newLine();
                }
                readLine = stdStream.readLine();
            }
        } catch (IOException e) {
            throw new ConfigFileIOException("Config file cannot be written.", e);
        } catch (NullPointerException e) {
            throw new InvalidDefaultConfigFileException("The default configuration file is missing" + "\nYour version of TOM is corrupt.", e);
        } finally {
            try {
                stdStream.close();
            } catch (Exception e) {
            }
            try {
                writeStream.close();
            } catch (Exception e) {
            }
        }
    }
