    protected CMLSpectrum readSpectrum(int num) throws Exception {
        CMLSpectrum spectrum = null;
        URL spurl = makeSpectrumInputStreamContainer(num);
        InputStream in = spurl.openStream();
        spectrum = (CMLSpectrum) new CMLBuilder().build(in).getRootElement();
        in.close();
        return spectrum;
    }
