    private void reading() throws IOException, FamiliesTreeFormatException {
        float last_percent = 0;
        float percent = 0;
        while (br.ready()) {
            String line;
            line = br.readLine();
            if (line != null && !line.equals("")) {
                String[] families = line.split(";");
                for (String family : families) {
                    DataParser.getInstance().readFamiliesTreeString(family);
                }
                current = fis.getChannel().position();
                percent = current * 100 / (float) max;
                if (percent > last_percent + 1) {
                    last_percent = percent;
                    taskMonitor.setPercentCompleted(Math.round(percent));
                }
            }
        }
    }
