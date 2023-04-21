        public void load(Serializer serializer, String target, ServerDetails existing) throws Exception {
            URL url = new URL(target);
            InputStream in = url.openStream();
            serializer.read(existing, in, false);
            in.close();
        }
