    public void parseImpl(String pathToFile) throws IOException, SQLException, ParseLineException {
        File file = new File(pathToFile);
        if (!file.exists()) {
            logger.warn("Skipping loading file '{}'", pathToFile);
            return;
        }
        FileInputStream finp = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(finp, Config.getInstance().getDefaultEncoding()));
        int totalSize = (int) finp.getChannel().size();
        int currentSize = 0;
        IVisual progress = Config.getInstance().getProgressInstance();
        progress.processStart(false);
        try {
            String line;
            int pos = 0;
            while ((line = reader.readLine()) != null) {
                this.readLine(++pos, line);
                currentSize += line.length() + "\n".length();
                progress.process(currentSize, totalSize);
            }
            progress.processOver();
        } finally {
            reader.close();
        }
    }
