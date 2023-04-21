    public Evaluation(String fileName) {
        if (fileName != "") {
            try {
                InputStream inputStream;
                if (fileName.startsWith("http://")) {
                    URL address = new URL(fileName);
                    URLConnection urlConnection = address.openConnection();
                    inputStream = urlConnection.getInputStream();
                } else {
                    inputStream = new FileInputStream(fileName);
                }
                CSVParser csvparser = new CSVParser(inputStream, "", "", "");
                csvparser.changeDelimiter(';');
                String array[][] = csvparser.getAllValues();
                for (int i = 0; i < (array.length); i++) {
                    if ((array[i].length < 3) || (array[i][2].equals("0") == false)) {
                        String mapping = array[i][0] + array[i][1];
                        mappings.add(mapping);
                        size++;
                    }
                }
                mappingsfound = true;
            } catch (Exception e) {
                UserInterface.print(e.getMessage());
            }
        }
    }
