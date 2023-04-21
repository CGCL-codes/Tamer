    private void parse(final URL url) throws IOException {
        logger.log(Component.POLICY, "reading policy file from {0}", url);
        final StreamTokenizer in = new StreamTokenizer(new InputStreamReader(url.openStream()));
        in.resetSyntax();
        in.slashSlashComments(true);
        in.slashStarComments(true);
        in.wordChars('A', 'Z');
        in.wordChars('a', 'z');
        in.wordChars('0', '9');
        in.wordChars('.', '.');
        in.wordChars('_', '_');
        in.wordChars('$', '$');
        in.whitespaceChars(' ', ' ');
        in.whitespaceChars('\t', '\t');
        in.whitespaceChars('\f', '\f');
        in.whitespaceChars('\n', '\n');
        in.whitespaceChars('\r', '\r');
        in.quoteChar('\'');
        in.quoteChar('"');
        int tok;
        int state = STATE_BEGIN;
        List keystores = new LinkedList();
        URL currentBase = null;
        List currentCerts = new LinkedList();
        Permissions currentPerms = new Permissions();
        while ((tok = in.nextToken()) != StreamTokenizer.TT_EOF) {
            switch(tok) {
                case '{':
                    if (state != STATE_GRANT) error(url, in, "spurious '{'");
                    state = STATE_PERMS;
                    tok = in.nextToken();
                    break;
                case '}':
                    if (state != STATE_PERMS) error(url, in, "spurious '}'");
                    state = STATE_BEGIN;
                    currentPerms.setReadOnly();
                    Certificate[] c = null;
                    if (!currentCerts.isEmpty()) c = (Certificate[]) currentCerts.toArray(new Certificate[currentCerts.size()]);
                    cs2pc.put(new CodeSource(currentBase, c), currentPerms);
                    currentCerts.clear();
                    currentPerms = new Permissions();
                    currentBase = null;
                    tok = in.nextToken();
                    if (tok != ';') in.pushBack();
                    continue;
            }
            if (tok != StreamTokenizer.TT_WORD) {
                error(url, in, "expecting word token");
            }
            if (in.sval.equalsIgnoreCase("keystore")) {
                String alg = KeyStore.getDefaultType();
                tok = in.nextToken();
                if (tok != '"' && tok != '\'') error(url, in, "expecting key store URL");
                String store = in.sval;
                tok = in.nextToken();
                if (tok == ',') {
                    tok = in.nextToken();
                    if (tok != '"' && tok != '\'') error(url, in, "expecting key store type");
                    alg = in.sval;
                    tok = in.nextToken();
                }
                if (tok != ';') error(url, in, "expecting semicolon");
                try {
                    KeyStore keystore = KeyStore.getInstance(alg);
                    keystore.load(new URL(url, store).openStream(), null);
                    keystores.add(keystore);
                } catch (Exception x) {
                    error(url, in, x.toString());
                }
            } else if (in.sval.equalsIgnoreCase("grant")) {
                if (state != STATE_BEGIN) error(url, in, "extraneous grant keyword");
                state = STATE_GRANT;
            } else if (in.sval.equalsIgnoreCase("signedBy")) {
                if (state != STATE_GRANT && state != STATE_PERMS) error(url, in, "spurious 'signedBy'");
                if (keystores.isEmpty()) error(url, in, "'signedBy' with no keystores");
                tok = in.nextToken();
                if (tok != '"' && tok != '\'') error(url, in, "expecting signedBy name");
                StringTokenizer st = new StringTokenizer(in.sval, ",");
                while (st.hasMoreTokens()) {
                    String alias = st.nextToken();
                    for (Iterator it = keystores.iterator(); it.hasNext(); ) {
                        KeyStore keystore = (KeyStore) it.next();
                        try {
                            if (keystore.isCertificateEntry(alias)) currentCerts.add(keystore.getCertificate(alias));
                        } catch (KeyStoreException kse) {
                            error(url, in, kse.toString());
                        }
                    }
                }
                tok = in.nextToken();
                if (tok != ',') {
                    if (state != STATE_GRANT) error(url, in, "spurious ','");
                    in.pushBack();
                }
            } else if (in.sval.equalsIgnoreCase("codeBase")) {
                if (state != STATE_GRANT) error(url, in, "spurious 'codeBase'");
                tok = in.nextToken();
                if (tok != '"' && tok != '\'') error(url, in, "expecting code base URL");
                String base = expand(in.sval);
                if (File.separatorChar != '/') base = base.replace(File.separatorChar, '/');
                try {
                    currentBase = new URL(base);
                } catch (MalformedURLException mue) {
                    error(url, in, mue.toString());
                }
                tok = in.nextToken();
                if (tok != ',') in.pushBack();
            } else if (in.sval.equalsIgnoreCase("principal")) {
                if (state != STATE_GRANT) error(url, in, "spurious 'principal'");
                tok = in.nextToken();
                if (tok == StreamTokenizer.TT_WORD) {
                    tok = in.nextToken();
                    if (tok != '"' && tok != '\'') error(url, in, "expecting principal name");
                    String name = in.sval;
                    Principal p = null;
                    try {
                        Class pclass = Class.forName(in.sval);
                        Constructor c = pclass.getConstructor(new Class[] { String.class });
                        p = (Principal) c.newInstance(new Object[] { name });
                    } catch (Exception x) {
                        error(url, in, x.toString());
                    }
                    for (Iterator it = keystores.iterator(); it.hasNext(); ) {
                        KeyStore ks = (KeyStore) it.next();
                        try {
                            for (Enumeration e = ks.aliases(); e.hasMoreElements(); ) {
                                String alias = (String) e.nextElement();
                                if (ks.isCertificateEntry(alias)) {
                                    Certificate cert = ks.getCertificate(alias);
                                    if (!(cert instanceof X509Certificate)) continue;
                                    if (p.equals(((X509Certificate) cert).getSubjectDN()) || p.equals(((X509Certificate) cert).getSubjectX500Principal())) currentCerts.add(cert);
                                }
                            }
                        } catch (KeyStoreException kse) {
                            error(url, in, kse.toString());
                        }
                    }
                } else if (tok == '"' || tok == '\'') {
                    String alias = in.sval;
                    for (Iterator it = keystores.iterator(); it.hasNext(); ) {
                        KeyStore ks = (KeyStore) it.next();
                        try {
                            if (ks.isCertificateEntry(alias)) currentCerts.add(ks.getCertificate(alias));
                        } catch (KeyStoreException kse) {
                            error(url, in, kse.toString());
                        }
                    }
                } else error(url, in, "expecting principal");
                tok = in.nextToken();
                if (tok != ',') in.pushBack();
            } else if (in.sval.equalsIgnoreCase("permission")) {
                if (state != STATE_PERMS) error(url, in, "spurious 'permission'");
                tok = in.nextToken();
                if (tok != StreamTokenizer.TT_WORD) error(url, in, "expecting permission class name");
                String className = in.sval;
                Class clazz = null;
                try {
                    clazz = Class.forName(className);
                } catch (ClassNotFoundException cnfe) {
                }
                tok = in.nextToken();
                if (tok == ';') {
                    if (clazz == null) {
                        currentPerms.add(new UnresolvedPermission(className, null, null, (Certificate[]) currentCerts.toArray(new Certificate[currentCerts.size()])));
                        continue;
                    }
                    try {
                        currentPerms.add((Permission) clazz.newInstance());
                    } catch (Exception x) {
                        error(url, in, x.toString());
                    }
                    continue;
                }
                if (tok != '"' && tok != '\'') error(url, in, "expecting permission target");
                String target = expand(in.sval);
                tok = in.nextToken();
                if (tok == ';') {
                    if (clazz == null) {
                        currentPerms.add(new UnresolvedPermission(className, target, null, (Certificate[]) currentCerts.toArray(new Certificate[currentCerts.size()])));
                        continue;
                    }
                    try {
                        Constructor c = clazz.getConstructor(new Class[] { String.class });
                        currentPerms.add((Permission) c.newInstance(new Object[] { target }));
                    } catch (Exception x) {
                        error(url, in, x.toString());
                    }
                    continue;
                }
                if (tok != ',') error(url, in, "expecting ','");
                tok = in.nextToken();
                if (tok == StreamTokenizer.TT_WORD) {
                    if (!in.sval.equalsIgnoreCase("signedBy")) error(url, in, "expecting 'signedBy'");
                    try {
                        Constructor c = clazz.getConstructor(new Class[] { String.class });
                        currentPerms.add((Permission) c.newInstance(new Object[] { target }));
                    } catch (Exception x) {
                        error(url, in, x.toString());
                    }
                    in.pushBack();
                    continue;
                }
                if (tok != '"' && tok != '\'') error(url, in, "expecting permission action");
                String action = in.sval;
                if (clazz == null) {
                    currentPerms.add(new UnresolvedPermission(className, target, action, (Certificate[]) currentCerts.toArray(new Certificate[currentCerts.size()])));
                    continue;
                } else {
                    try {
                        Constructor c = clazz.getConstructor(new Class[] { String.class, String.class });
                        currentPerms.add((Permission) c.newInstance(new Object[] { target, action }));
                    } catch (Exception x) {
                        error(url, in, x.toString());
                    }
                }
                tok = in.nextToken();
                if (tok != ';' && tok != ',') error(url, in, "expecting ';' or ','");
            }
        }
    }
