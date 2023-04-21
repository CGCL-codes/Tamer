        public void seek(long pos) throws IOException {
            fis.getChannel().position(pos);
            this.position = pos;
        }
