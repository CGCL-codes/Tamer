        public boolean fetch() throws Exception {
            JarEntry entry;
            while ((entry = jarStream.getNextJarEntry()) != null && entry.isDirectory()) ;
            if (entry == null) return false;
            Blob blob = conn.createBlob();
            OutputStream out = blob.setBinaryStream(1);
            try {
                for (int count; (count = jarStream.read(buffer)) != -1; ) out.write(buffer, 0, count);
            } finally {
                out.close();
            }
            className[0] = entry.getName();
            entryStmt.setString(2, className[0]);
            entryStmt.setBlob(3, blob);
            entryStmt.execute();
            jarStream.closeEntry();
            return true;
        }
