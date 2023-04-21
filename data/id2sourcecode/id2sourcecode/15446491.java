        private void flushchars() {
            try {
                flushing = true;
                for (; reader.ready(); reader.read(cbuf, cend++, 1)) ensurec(1);
                unreader.write(cbuf, cstart, cend);
                cstart = cend = 0;
                unreader.flush();
            } catch (IOException e) {
                ioe(e);
            } finally {
                flushing = false;
            }
        }
