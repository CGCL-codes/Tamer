        @Override
        protected Expression instantiate(Object oldInstance, Encoder out) {
            BasicChromatogram chromatogram = (BasicChromatogram) oldInstance;
            return new Expression(chromatogram, ChromatogramXMLSerializer.class, "buildBasicChromatogram", new Object[] { NucleotideGlyph.convertToString(chromatogram.getBasecalls().decode()), new EncodedByteData(PhredQuality.toArray(chromatogram.getQualities().decode())).encodeData(), chromatogram.getPeaks(), chromatogram.getChannelGroup(), chromatogram.getProperties() });
        }
