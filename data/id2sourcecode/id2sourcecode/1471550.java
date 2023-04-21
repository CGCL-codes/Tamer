    public void sendFileContents(String fileName) {
        httpGenerator gen = new httpGenerator();
        int count = 0;
        try {
            FileInputStream inFile = new FileInputStream(fileName);
            gen.addServer();
            gen.addContentType(fileName);
            sock.writeLine(gen.getResponseHeader());
            if (debugEnabled) System.out.println(gen.getResponseHeader());
            byte[] buffer = new byte[16384];
            while ((count = inFile.read(buffer)) > 0) sock.write(buffer, 0, count);
            inFile.close();
        } catch (FileNotFoundException e) {
            gen.add404NotFound();
            gen.addContentType(eContentTypes.HTML.toExt());
            sock.writeLine(gen.getResponseHeader());
            if (debugEnabled) System.out.println(gen.getResponseHeader());
            sock.writeLine(httpGenerator.getNotFoundResponse());
        } catch (IOException e) {
            sock.writeLine(e.getMessage());
        }
    }
