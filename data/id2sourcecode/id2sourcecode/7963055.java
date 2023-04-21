    public void service(InputStream inputStream, OutputStream outputStream) throws ServiceException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        PrintWriter writer = new PrintWriter(outputStream);
        try {
            writer.println(reader.readLine());
            writer.flush();
            writer.close();
            reader.close();
        } catch (IOException e) {
            throw new ServiceException(e);
        }
    }
