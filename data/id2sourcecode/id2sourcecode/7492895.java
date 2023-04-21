    public void decode(InputStream compressedData, OutputStream result, COSDictionary options) throws IOException {
        COSDictionary dict = (COSDictionary) options.getDictionaryObject("DecodeParms");
        int width = options.getInt("Width");
        int height = options.getInt("Height");
        int length = options.getInt(COSName.LENGTH);
        int compressionType = dict.getInt("K");
        boolean blackIs1 = dict.getBoolean("BlackIs1", false);
        writeTagHeader(result, length);
        int i = 0;
        byte[] buffer = new byte[32768];
        int lentoread = length;
        while ((lentoread > 0) && ((i = compressedData.read(buffer, 0, Math.min(lentoread, 32768))) != -1)) {
            result.write(buffer, 0, i);
            lentoread = lentoread - i;
        }
        while (lentoread > 0) {
            result.write(buffer, 0, Math.min(lentoread, 32768));
            lentoread = lentoread - Math.min(lentoread, 32738);
        }
        writeTagCount(result);
        writeTagWidth(result, width);
        writeTagHeight(result, height);
        writeTagBitsPerSample(result, 1);
        writeTagCompression(result, compressionType);
        writeTagPhotometric(result, blackIs1);
        writeTagStripOffset(result, 8);
        writeTagOrientation(result, 1);
        writeTagSamplesPerPixel(result, 1);
        writeTagRowsPerStrip(result, height);
        writeTagStripByteCount(result, length);
        writeTagXRes(result, 200, 1);
        writeTagYRes(result, 200, 1);
        writeTagResolutionUnit(result, 2);
        writeTagSoftware(result, "pdfbox".getBytes());
        writeTagDateTime(result, new Date());
        writeTagTailer(result);
    }
