        public boolean filter(Object provider) {
            if (provider instanceof ImageTranscoderSpi) {
                ImageTranscoderSpi spi = (ImageTranscoderSpi) provider;
                if (spi.getReaderServiceProviderName().equals(reader.getOriginatingProvider().getClass().getName()) && spi.getWriterServiceProviderName().equals(writer.getOriginatingProvider().getClass().getName())) return true;
            }
            return false;
        }
