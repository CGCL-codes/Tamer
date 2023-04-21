    private void writeEvidenceDbxref(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            String data[] = line.split(SEPARATOR_TAB);
            Evidence evidence = (Evidence) session.createQuery("from go_evidence a where a.id = :id").setParameter("id", Integer.valueOf(data[0].trim())).uniqueResult();
            Dbxref dxref = (Dbxref) session.createQuery("from go_dbxref a where a.id = :id").setParameter("id", Integer.valueOf(data[1].trim())).uniqueResult();
            if (evidence != null && dxref != null) {
                EvidenceDbxref entry = new EvidenceDbxref();
                entry.setEvidenceId(evidence);
                entry.setDbxrefId(dxref);
                try {
                    insertObject(entry);
                } catch (HibernateException e) {
                    Log.writeWarningLog(this.getClass(), e.getMessage(), e);
                }
            }
        }
        reader.close();
    }
