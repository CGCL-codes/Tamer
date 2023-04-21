        private HTTPResponse fileResponse(String path) throws IOException {
            File file = new File(path);
            ByteArrayOutputStream to = new ByteArrayOutputStream();
            FileInputStream from = new FileInputStream(file);
            ZipUtil.pipeStreams(to, from);
            from.close();
            return new HTTPResponse(HTTPResponse.RESPONSE_OK, "application/unknown", to.size(), to.toByteArray());
        }
