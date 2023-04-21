        protected byte[] loadClassImage(String name) throws ClassNotFoundException {
            String path = warDir.getAbsolutePath() + "/WEB-INF/classes/" + name.replace(".", "/") + ".class";
            FileInputStream in = null;
            try {
                in = new FileInputStream(path);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte bs[] = new byte[1024];
                int count;
                while ((count = in.read(bs)) > 0) out.write(bs, 0, count);
                return out.toByteArray();
            } catch (FileNotFoundException e) {
                throw new ClassNotFoundException(name);
            } catch (IOException e) {
                throw new ClassNotFoundException(name);
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
