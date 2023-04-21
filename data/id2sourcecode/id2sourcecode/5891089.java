    public static String getAstString(IFile file) throws CoreException, IOException {
        InputStreamReader reader = new InputStreamReader(file.getContents(), file.getCharset());
        try {
            StringWriter sw = new StringWriter();
            while (true) {
                int read = reader.read();
                if (read < 0) break;
                sw.write(read);
            }
            return getAstString(sw.toString());
        } finally {
            reader.close();
        }
    }
