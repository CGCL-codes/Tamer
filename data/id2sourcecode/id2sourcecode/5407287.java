    private void CertDialog(X509Certificate c) throws CertificateException {
        int i;
        boolean expired = false;
        boolean notyet = false;
        Vector<String> reason = new Vector<String>();
        reason.add(this.uploadPolicy.getString("itm_reason_itrust"));
        try {
            c.checkValidity();
        } catch (CertificateExpiredException e1) {
            expired = true;
            reason.add(this.uploadPolicy.getString("itm_reason_expired"));
        } catch (CertificateNotYetValidException e2) {
            notyet = true;
            reason.add(this.uploadPolicy.getString("itm_reason_notyet"));
        }
        StringBuffer msg = new StringBuffer();
        msg.append("<html><head>");
        msg.append("<style type=\"text/css\">\n");
        msg.append("td, th, p, body { ");
        msg.append("font-family: Arial, Helvetica, sans-serif; ");
        msg.append("font-size: 12pt; ");
        Integer ii = new Integer(new JButton(".").getForeground().getRGB() & 0x00ffffff);
        msg.append("color: ").append(String.format("#%06x", ii)).append(" }\n");
        msg.append("th { text-align: left; }\n");
        msg.append("td { margin-left: 20; }\n");
        msg.append(".err { color: red; }\n");
        msg.append("</style>\n");
        msg.append("</head><body>");
        msg.append("<h3>").append(this.uploadPolicy.getString("itm_fail_verify")).append("</h3>");
        msg.append("<h4>").append(this.uploadPolicy.getString("itm_cert_details")).append("</h4>");
        msg.append("<table>");
        msg.append("<tr><th colspan=2>").append(this.uploadPolicy.getString("itm_cert_subject")).append("</th></tr>");
        msg.append(formatDN(c.getSubjectX500Principal().getName(), this.hostname, reason));
        msg.append("<tr><td>").append(this.uploadPolicy.getString("itm_cert_nbefore")).append("</td>");
        msg.append(notyet ? "<td class=\"err\">" : "<td>").append(c.getNotBefore()).append("</td></tr>\n");
        msg.append("<tr><td>").append(this.uploadPolicy.getString("itm_cert_nafter")).append("</td>");
        msg.append(expired ? "<td class=\"err\">" : "<td>").append(c.getNotAfter()).append("</td></tr>\n");
        msg.append("<tr><td>").append(this.uploadPolicy.getString("itm_cert_serial")).append("</td><td>");
        msg.append(c.getSerialNumber());
        msg.append("</td></tr>\n");
        msg.append("<tr><td>").append(String.format(this.uploadPolicy.getString("itm_cert_fprint"), "SHA1")).append("</td><td>");
        MessageDigest d;
        StringBuffer fp = new StringBuffer();
        try {
            d = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new CertificateException("Unable to calculate certificate SHA1 fingerprint: " + e.getMessage());
        }
        byte[] sha1sum = d.digest(c.getEncoded());
        for (i = 0; i < sha1sum.length; i++) {
            if (i > 0) fp.append(":");
            fp.append(Integer.toHexString((sha1sum[i] >> 4) & 0x0f));
            fp.append(Integer.toHexString(sha1sum[i] & 0x0f));
        }
        msg.append(fp).append("</td></tr>\n");
        fp.setLength(0);
        msg.append("<tr><td>").append(String.format(this.uploadPolicy.getString("itm_cert_fprint"), "MD5")).append("</td><td>");
        try {
            d = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new CertificateException("Unable to calculate certificate MD5 fingerprint: " + e.getMessage());
        }
        byte[] md5sum = d.digest(c.getEncoded());
        for (i = 0; i < md5sum.length; i++) {
            if (i > 0) fp.append(":");
            fp.append(Integer.toHexString((md5sum[i] >> 4) & 0x0f));
            fp.append(Integer.toHexString(md5sum[i] & 0x0f));
        }
        msg.append(fp).append("</td></tr>\n");
        msg.append("</table><table>");
        msg.append("<tr><th colspan=2>").append(this.uploadPolicy.getString("itm_cert_issuer")).append("</th></tr>");
        msg.append(formatDN(c.getIssuerX500Principal().getName(), null, reason));
        msg.append("</table>");
        msg.append("<p><b>").append(this.uploadPolicy.getString("itm_reasons")).append("</b><br><ul>");
        Iterator it = reason.iterator();
        while (it.hasNext()) {
            msg.append("<li>" + it.next() + "</li>\n");
        }
        msg.append("</ul></p>");
        msg.append("<p><b>").append(this.uploadPolicy.getString("itm_accept_prompt")).append("</b></p>");
        msg.append("</body></html>");
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        JEditorPane ep = new JEditorPane("text/html", msg.toString());
        ep.setEditable(false);
        ep.setBackground(p.getBackground());
        p.add(ep, BorderLayout.CENTER);
        String no = this.uploadPolicy.getString("itm_accept_no");
        int ans = JOptionPane.showOptionDialog(null, p, "SSL Certificate Alert", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[] { this.uploadPolicy.getString("itm_accept_always"), this.uploadPolicy.getString("itm_accept_now"), no }, no);
        switch(ans) {
            case JOptionPane.CANCEL_OPTION:
            case JOptionPane.CLOSED_OPTION:
                throw new CertificateException("Server certificate rejected.");
            case JOptionPane.NO_OPTION:
            case JOptionPane.YES_OPTION:
                try {
                    ts.setCertificateEntry(fp.toString(), c);
                } catch (KeyStoreException e) {
                    throw new CertificateException("Unable to add certificate: " + e.getMessage());
                }
                if (ans == JOptionPane.YES_OPTION) {
                    if (null == System.getProperty(TSKEY)) this.tsname = USERTS;
                    while (true) {
                        try {
                            File f = new File(this.tsname);
                            boolean old = false;
                            if (f.exists()) {
                                if (!f.renameTo(new File(this.tsname + ".old"))) throw new IOException("Could not rename truststore");
                                old = true;
                            } else {
                                this.tspasswd = this.getPassword(this.uploadPolicy.getString("itm_new_tstore"));
                                if (null == this.tspasswd) this.tspasswd = "changeit";
                            }
                            FileOutputStream os = new FileOutputStream(this.tsname);
                            ts.store(os, this.tspasswd.toCharArray());
                            os.close();
                            if (old && (!f.delete())) throw new IOException("Could not delete old truststore");
                            this.tmf.init(ts);
                            System.out.println("Saved cert to " + this.tsname);
                            break;
                        } catch (Exception e) {
                            if (this.tsname.equals(USERTS)) throw new CertificateException(e);
                            this.tsname = USERTS;
                        }
                    }
                }
        }
    }
