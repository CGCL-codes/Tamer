    public void loadWriterData() throws GridException {
        rasterBuf = RasterBuffer.getBuffer(dataType, reader.getNX(), reader.getNY(), bands.length, true);
        writer = new GridWriter(windowExtent, dataType, rasterBuf);
        int x = 0, y = 0;
        try {
            switch(rasterBuf.getDataType()) {
                case IBuffer.TYPE_BYTE:
                    for (x = 0; x < getNX(); x++) {
                        for (y = 0; y < getNY(); y++) writer.setCellValue(x, y, (byte) (reader.getCellValueAsByte(x, y)));
                    }
                    break;
                case IBuffer.TYPE_SHORT:
                    for (x = 0; x < getNX(); x++) {
                        for (y = 0; y < getNY(); y++) writer.setCellValue(x, y, (short) (reader.getCellValueAsShort(x, y)));
                    }
                    break;
                case IBuffer.TYPE_INT:
                    for (x = 0; x < getNX(); x++) {
                        for (y = 0; y < getNY(); y++) writer.setCellValue(x, y, (int) (reader.getCellValueAsInt(x, y)));
                    }
                    break;
                case IBuffer.TYPE_FLOAT:
                    for (x = 0; x < getNX(); x++) {
                        for (y = 0; y < getNY(); y++) writer.setCellValue(x, y, (float) (reader.getCellValueAsFloat(x, y)));
                    }
                    break;
                case IBuffer.TYPE_DOUBLE:
                    for (x = 0; x < getNX(); x++) {
                        for (y = 0; y < getNY(); y++) writer.setCellValue(x, y, (double) (reader.getCellValueAsDouble(x, y)));
                    }
                    break;
            }
        } catch (RasterBufferInvalidException e) {
            throw new GridException("Buffer de datos no v�lido " + x + " " + y, e);
        } catch (OutOfGridException e1) {
            throw new GridException("Acceso fuera de los l�mites del Grid " + x + " " + y, e1);
        } catch (RasterBufferInvalidAccessException e) {
            throw new GridException("Acceso al grid no v�lido " + x + " " + y, e);
        } catch (InterruptedException e) {
        }
    }
