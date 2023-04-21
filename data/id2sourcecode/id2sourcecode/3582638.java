    private JResponseBean postFile(File file, JConfigAtom atom) throws Exception {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int readed;
        JMultipartHttpFilePost connection = new JMultipartHttpFilePost(atom.getUrl());
        FileInputStream stream = new FileInputStream(file);
        connection.startFile("file" + atom.hashCode(), "file.xml");
        try {
            OutputStream out = connection.getOutputStream();
            while ((readed = stream.read(buffer, 0, buffer.length)) >= 0) {
                out.write(buffer, 0, readed);
            }
            out.flush();
            JBrtFormatter.writeLogEnd(new PrintStream(out, true, ENCODING));
            out.flush();
        } finally {
            try {
                connection.endFile();
            } finally {
                stream.close();
            }
        }
        return JBrtFormatter.convertXMLToJResponse(connection.post());
    }
