    public Document fetchDocument(String documentName) {
        Document doc = (Document) documents.get(documentName);
        if (doc != null) {
            if (CACHE_TIMEOUT > 0) if (doc.lastAccess < (System.currentTimeMillis() - CACHE_TIMEOUT)) doc = null;
            if (doc != null) {
                fifo.remove(doc);
                fifo.put(doc);
            }
        }
        if (doc != null) {
            doc.touch();
            return doc;
        }
        if (!documentName.startsWith("/")) return null;
        InputStream in = null;
        boolean defaultPage;
        for (int i = 0; i < documentBases.length; i++) {
            defaultPage = false;
            if (documentBases[i] instanceof ZipFile) {
                ZipFile zipArchive = (ZipFile) documentBases[i];
                String relativeDocumentName = documentName.substring(1, documentName.length());
                ZipEntry entry = zipArchive.getEntry(relativeDocumentName);
                if (entry.getSize() == 0) entry = zipArchive.getEntry(relativeDocumentName + "/index.html");
                if (entry == null) continue;
                try {
                    in = new BufferedInputStream(zipArchive.getInputStream(entry));
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    continue;
                }
            } else if (documentBases[i] instanceof File) {
                try {
                    File docBase = (File) documentBases[i];
                    File docFile = new File(docBase, documentName);
                    if (docFile.isDirectory()) {
                        docFile = new File(docFile, "index.html");
                        defaultPage = true;
                    }
                    if (!docFile.exists()) continue;
                    if (!docFile.getCanonicalPath().startsWith(docBase.getAbsolutePath())) {
                        Debug.println(Debug.ERROR, this, "Security Warning:");
                        Debug.println(Debug.ERROR, this, "Illegal attempt to access document: " + docFile.getCanonicalPath());
                        Debug.println(Debug.ERROR, this, "(Document not in '" + docBase.getAbsolutePath() + "')");
                        continue;
                    }
                    in = new BufferedInputStream(new FileInputStream(docFile));
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    continue;
                }
            }
            if (in != null) {
                try {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) > 0) out.write(buffer, 0, len);
                    if (defaultPage) doc = new Document(documentName, out.toByteArray(), Document.TEXT_HTML); else doc = new Document(documentName, out.toByteArray());
                    addDocument(doc);
                    return doc;
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    continue;
                }
            }
        }
        return null;
    }
