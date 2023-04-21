    private void writeResource(Writer writer, String resourceName) throws IOException {
        InputStream helpStream = classpathStream(resourceName);
        Scanner scanner = new Scanner(helpStream, "UTF-8");
        try {
            while (scanner.hasNextLine()) {
                writer.append(scanner.nextLine());
            }
        } finally {
            scanner.close();
            writer.flush();
        }
    }
