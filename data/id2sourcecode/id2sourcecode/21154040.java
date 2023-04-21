    public void writeLoop() throws IOException {
        decorated.writeBody(file);
        Reader r = new InputStreamReader(file.getInputStream());
        char buff[] = new char[org.makumba.Text.FILE_LIMIT];
        int n;
        while ((n = r.read(buff)) > 0) decorated.bodyContent.getEnclosingWriter().write(buff, 0, n);
        r.close();
    }
