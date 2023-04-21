    public static String loadResourceAsString(String name) {
        URL url = Loader.class.getClassLoader().getResource(name);
        InputStream urlInput = null;
        try {
            StringWriter stringWriter = new StringWriter();
            BufferedWriter writer = new BufferedWriter(stringWriter);
            urlInput = url.openStream();
            Scanner scanner = new Scanner(urlInput);
            while (scanner.hasNextLine()) {
                writer.append(scanner.nextLine());
                if (scanner.hasNextLine()) {
                    writer.newLine();
                }
            }
            writer.close();
            return stringWriter.getBuffer().toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlInput != null) {
                try {
                    urlInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
