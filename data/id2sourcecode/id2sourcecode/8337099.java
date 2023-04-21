    public static void flushClose() throws IOException {
        if (fout != null) {
            fout.write("</geoParsedDocs>".getBytes());
            fout.flush();
            System.out.println("Channel Size:" + fout.getChannel().size());
            fout.close();
            fout = null;
        }
    }
