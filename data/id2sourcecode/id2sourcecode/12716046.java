    private Sequence exifToolWebExtract(URI uri) throws XPathException {
        ExiftoolModule module = (ExiftoolModule) getParentModule();
        InputStream stdIn = null;
        ByteArrayOutputStream baos = null;
        try {
            Process p = Runtime.getRuntime().exec(module.getExiftoolPath() + " -fast -X -");
            stdIn = p.getInputStream();
            OutputStream stdOut = p.getOutputStream();
            Source src = SourceFactory.getSource(context.getBroker(), null, uri.toString(), false);
            InputStream isSrc = src.getInputStream();
            int read = -1;
            byte buf[] = new byte[4096];
            while ((read = isSrc.read(buf)) > -1) {
                stdOut.write(buf, 0, read);
            }
            stdOut.flush();
            stdOut.close();
            baos = new ByteArrayOutputStream();
            read = -1;
            while ((read = stdIn.read(buf)) > -1) {
                baos.write(buf, 0, read);
            }
            p.waitFor();
            return ModuleUtils.inputSourceToXML(context, new InputSource(new ByteArrayInputStream(baos.toByteArray())));
        } catch (IOException ex) {
            throw new XPathException("Could not execute the Exiftool " + ex.getMessage(), ex);
        } catch (PermissionDeniedException pde) {
            throw new XPathException("Could not execute the Exiftool " + pde.getMessage(), pde);
        } catch (SAXException saxe) {
            LOG.error("exiftool returned=" + new String(baos.toByteArray()), saxe);
            throw new XPathException("Could not parse output from the Exiftool " + saxe.getMessage(), saxe);
        } catch (InterruptedException ie) {
            throw new XPathException("Could not execute the Exiftool " + ie.getMessage(), ie);
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException ioe) {
                }
            }
            if (stdIn != null) {
                try {
                    stdIn.close();
                } catch (IOException ioe) {
                }
            }
        }
    }
