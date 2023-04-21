        public void interceptOutput(HttpServletResponse response, InputStream in, OutputStream out, Object fromInput) throws IOException {
            int numBytesRead;
            while ((numBytesRead = in.read(buffer, 0, buffer.length)) > 0) out.write(buffer, 0, numBytesRead);
        }
