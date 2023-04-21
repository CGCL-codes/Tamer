    public static void writeResourceToFile(File file, String resourceName, Class<?> clasz) throws IOException {
        InputStream inputStream = clasz.getResourceAsStream("/" + resourceName);
        if (inputStream == null) {
            System.err.println("Couldn't find resource on the class path: " + resourceName);
        } else {
            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                try {
                    int nread;
                    byte[] buffer = new byte[4096];
                    while (0 < (nread = inputStream.read(buffer))) {
                        outputStream.write(buffer, 0, nread);
                    }
                } finally {
                    outputStream.close();
                }
            } finally {
                inputStream.close();
            }
        }
    }
