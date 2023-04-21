    private static String loadScript(String path) throws IOException {
        Reader reader = null;
        try {
            StringWriter sw = new StringWriter();
            reader = new BufferedReader(new FileReader(path));
            int c = -1;
            while ((c = reader.read()) != -1) sw.write(c);
            return sw.toString();
        } finally {
            if (reader != null) reader.close();
        }
    }
