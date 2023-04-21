    JarAgletClassLoader(URL codebase, Certificate cert) throws java.io.IOException {
        super(checkAndTrim(codebase), cert);
        this._jar = new com.ibm.awb.misc.JarArchive(codebase.openStream());
        Archive.Entry ae[] = this._jar.entries();
        this._digest_table = new DigestTable(ae.length);
        for (Entry element : ae) {
            this._digest_table.setDigest(element.name(), element.digest());
        }
    }
