        public Object nextElement() {
            Object result = next;
            data = new byte[1024];
            int read = -1;
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                do {
                    read = zipper.read(data, 0, 1024);
                    if (read > -1) bos.write(data, 0, read);
                } while (read > -1);
                data = bos.toByteArray();
                next = zipper.getNextEntry();
            } catch (Exception e) {
                System.out.println("Exception reading next entry " + e);
                e.printStackTrace();
                next = null;
            }
            return result;
        }
