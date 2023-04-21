        public CharBuffer getChars() {
            if (this.chars != null) return this.chars;
            CharArrayWriter out = new CharArrayWriter(this.bytes.capacity());
            Reader in = asReader();
            try {
                for (int c; (c = in.read()) >= 0; ) out.write(c);
            } catch (final IOException ex) {
                throw new RuntimeException(ex);
            }
            this.chars = CharBuffer.wrap(out.toCharArray(), 0, out.size());
            return this.chars.asReadOnlyBuffer();
        }
