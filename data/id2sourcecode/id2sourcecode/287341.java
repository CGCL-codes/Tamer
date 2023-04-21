    boolean processAttachment(Object res) {
        AttachmentPart at = null;
        if (res == null) return false; else if (res instanceof AttachmentPart) try {
            boolean ok = true;
            at = (AttachmentPart) res;
            attachments.remove(at);
            Object cont = null;
            try {
                javax.activation.DataHandler dh = at.getDataHandler();
                javax.activation.DataSource ds = dh.getDataSource();
                InputStream is = ds.getInputStream();
                if (doxmlstream && "text/xml".equals(ds.getContentType())) cont = new javax.xml.transform.stream.StreamSource(is); else cont = is;
            } catch (Exception dx) {
                if (debug) dx.printStackTrace();
                ok = false;
            }
            if (cont instanceof String) {
                if (((String) cont).length() == 0) ok = false;
                out.println(cont);
            } else if (cont instanceof javax.xml.transform.Source) {
                processXmlSource((javax.xml.transform.Source) cont);
            } else if (cont instanceof java.io.InputStream) {
                java.io.InputStream ins = (java.io.InputStream) cont;
                if (pipe == null) pipe = new byte[pipesize];
                int len;
                while ((len = ins.read(pipe)) >= 0) out.write(pipe, 0, len);
                out.flush();
                ins.close();
            } else {
                if (cont != null) debug("# attach content=" + cont.getClass().getName());
                out.println(cont);
            }
            at.dispose();
            return ok;
        } catch (Exception e) {
            err(">> attach err=" + e);
            if (at != null) at.dispose();
            return false;
        } else {
            out.println(res);
            return true;
        }
    }
