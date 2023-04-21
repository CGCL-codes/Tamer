    static boolean extractRevision(String extraction_path, String signed_doc, boolean verbose) {
        PdfReader reader;
        try {
            reader = new PdfReader(signed_doc);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        AcroFields af = reader.getAcroFields();
        ArrayList names = af.getSignatureNames();
        printf(verbose, "%d revision(s) found.", names.size());
        for (int k = 0; k < names.size(); ++k) {
            String name = (String) names.get(k);
            FileOutputStream out;
            try {
                out = new FileOutputStream(extraction_path + "revision_" + af.getRevision(name) + ".pdf");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            byte bb[] = new byte[8192];
            InputStream ip;
            try {
                ip = af.extractRevision(name);
                int n = 0;
                while ((n = ip.read(bb)) > 0) out.write(bb, 0, n);
                out.close();
                ip.close();
                println(verbose, Messages.getString("Notary.RevisionExtracted") + "revision_" + af.getRevision(name) + ".pdf");
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
