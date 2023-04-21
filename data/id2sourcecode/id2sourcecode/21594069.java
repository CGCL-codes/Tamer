    protected DataRaster doDecompress(Object source, AVList params) throws Exception {
        if (null == params || !params.hasKey(AVKey.SECTOR)) {
            String message = Logging.getMessage("generic.MissingRequiredParameter", AVKey.SECTOR);
            Logging.logger().severe(message);
            throw new WWRuntimeException(message);
        }
        File file = WWIO.getFileForLocalAddress(source);
        if (null == file) {
            String message = Logging.getMessage("generic.UnrecognizedSourceType", source.getClass().getName());
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        if (!file.exists()) {
            String message = Logging.getMessage("generic.FileNotFound", file.getAbsolutePath());
            Logging.logger().severe(message);
            throw new FileNotFoundException(message);
        }
        if (!file.canRead()) {
            String message = Logging.getMessage("generic.FileNoReadPermission", file.getAbsolutePath());
            Logging.logger().severe(message);
            throw new IOException(message);
        }
        RandomAccessFile raf = null;
        FileChannel channel = null;
        DataRaster raster = null;
        try {
            raf = new RandomAccessFile(file, "r");
            channel = raf.getChannel();
            java.nio.MappedByteBuffer buffer = this.mapFile(channel, 0, channel.size());
            buffer.position(0);
            DDSHeader header = DDSHeader.readFrom(source);
            int width = header.getWidth();
            int height = header.getHeight();
            if (!WWMath.isPowerOfTwo(width) || !WWMath.isPowerOfTwo(height)) {
                String message = Logging.getMessage("generic.InvalidImageSize", width, height);
                Logging.logger().severe(message);
                throw new WWRuntimeException(message);
            }
            int mipMapCount = header.getMipMapCount();
            DDSPixelFormat pixelFormat = header.getPixelFormat();
            if (null == pixelFormat) {
                String reason = Logging.getMessage("generic.MissingRequiredParameter", "DDSD_PIXELFORMAT");
                String message = Logging.getMessage("generic.InvalidImageFormat", reason);
                Logging.logger().severe(message);
                throw new WWRuntimeException(message);
            }
            DXTDecompressor decompressor = null;
            int dxtFormat = pixelFormat.getFourCC();
            if (dxtFormat == DDSConstants.D3DFMT_DXT3) {
                decompressor = new DXT3Decompressor();
            } else if (dxtFormat == DDSConstants.D3DFMT_DXT1) {
                decompressor = new DXT1Decompressor();
            }
            if (null == decompressor) {
                String message = Logging.getMessage("generic.UnsupportedCodec", dxtFormat);
                Logging.logger().severe(message);
                throw new WWRuntimeException(message);
            }
            Sector sector = (Sector) params.getValue(AVKey.SECTOR);
            params.setValue(AVKey.PIXEL_FORMAT, AVKey.IMAGE);
            if (mipMapCount == 0) {
                buffer.position(DDSConstants.DDS_DATA_OFFSET);
                BufferedImage image = decompressor.decompress(buffer, header.getWidth(), header.getHeight());
                raster = new BufferedImageRaster(sector, image, params);
            } else if (mipMapCount > 0) {
                ArrayList<BufferedImage> list = new ArrayList<BufferedImage>();
                int mmLength = header.getLinearSize();
                int mmOffset = DDSConstants.DDS_DATA_OFFSET;
                for (int i = 0; i < mipMapCount; i++) {
                    int zoomOut = (int) Math.pow(2d, (double) i);
                    int mmWidth = header.getWidth() / zoomOut;
                    int mmHeight = header.getHeight() / zoomOut;
                    if (mmWidth < 4 || mmHeight < 4) {
                        break;
                    }
                    buffer.position(mmOffset);
                    BufferedImage image = decompressor.decompress(buffer, mmWidth, mmHeight);
                    list.add(image);
                    mmOffset += mmLength;
                    mmLength /= 4;
                }
                BufferedImage[] images = new BufferedImage[list.size()];
                images = (BufferedImage[]) list.toArray(images);
                raster = new MipMappedBufferedImageRaster(sector, images);
            }
            return raster;
        } finally {
            String name = (null != file) ? file.getAbsolutePath() : ((null != source) ? source.toString() : "unknown");
            WWIO.closeStream(channel, name);
            WWIO.closeStream(raf, name);
        }
    }
