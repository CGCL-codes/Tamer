        public void write(File to) throws IOException {
            Properties props = new Properties();
            setFields(props, this);
            RandomAccessFile file = new RandomAccessFile(to, "rws");
            FileOutputStream out = null;
            try {
                file.seek(0);
                out = new FileOutputStream(file.getFD());
                props.store(out, null);
                file.setLength(out.getChannel().position());
            } finally {
                if (out != null) {
                    out.close();
                }
                file.close();
            }
        }
