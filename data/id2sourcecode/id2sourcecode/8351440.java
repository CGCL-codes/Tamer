    private void sendContent() throws IOException {
        if (contentBuffer instanceof ByteArrayOutputStream) {
            ((ByteArrayOutputStream) contentBuffer).writeTo(out);
            contentBuffer = null;
        } else if (largeOutputFile != null) {
            FileInputStream content = new FileInputStream(largeOutputFile);
            byte[] buf = new byte[2048];
            int bytesRead;
            while ((bytesRead = content.read(buf)) != -1) out.write(buf, 0, bytesRead);
            content.close();
            largeOutputFile.delete();
            largeOutputFile = null;
        }
    }
