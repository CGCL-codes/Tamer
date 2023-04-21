        public DumpRequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            setCharacterEncoding("UTF-8");
            InputStream is = null;
            try {
                is = request.getInputStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream(128);
                for (int b = is.read(); b != -1; b = is.read()) out.write(b);
                this.bytes = out.toByteArray();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
