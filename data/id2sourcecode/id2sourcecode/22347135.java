        public boolean apply(Object o) {
            return reader.supported().apply(o) && writer.supported().apply(o);
        }
