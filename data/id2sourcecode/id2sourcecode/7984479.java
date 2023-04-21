        protected byte[] loadClassImage(String name) throws ClassNotFoundException {
            String path = "/WEB-INF/classes/" + name.replace(".", "/") + ".class";
            ZipEntry entry = zip.getEntry(path);
            InputStream in = null;
            try {
                in = zip.getInputStream(entry);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte bs[] = new byte[1024];
                int count;
                while ((count = in.read(bs)) > 0) out.write(bs, 0, count);
                return out.toByteArray();
            } catch (IOException e) {
                throw new ClassNotFoundException(name);
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
