    public void multiply(double value) throws GridException {
        boolean interp = (reader instanceof GridInterpolated);
        switchToInterpolationMethod(false);
        try {
            switch(rasterBuf.getDataType()) {
                case IBuffer.TYPE_BYTE:
                    for (int x = 0; x < getNX(); x++) {
                        for (int y = 0; y < getNY(); y++) writer.setCellValue(x, y, (byte) (reader.getCellValueAsByte(x, y) * value));
                    }
                    break;
                case IBuffer.TYPE_SHORT:
                    for (int x = 0; x < getNX(); x++) {
                        for (int y = 0; y < getNY(); y++) writer.setCellValue(x, y, (short) (reader.getCellValueAsShort(x, y) * value));
                    }
                    break;
                case IBuffer.TYPE_INT:
                    for (int x = 0; x < getNX(); x++) {
                        for (int y = 0; y < getNY(); y++) writer.setCellValue(x, y, (int) (reader.getCellValueAsInt(x, y) * value));
                    }
                    break;
                case IBuffer.TYPE_FLOAT:
                    for (int x = 0; x < getNX(); x++) {
                        for (int y = 0; y < getNY(); y++) writer.setCellValue(x, y, (float) (reader.getCellValueAsFloat(x, y) * value));
                    }
                    break;
                case IBuffer.TYPE_DOUBLE:
                    for (int x = 0; x < getNX(); x++) {
                        for (int y = 0; y < getNY(); y++) writer.setCellValue(x, y, (double) (reader.getCellValueAsDouble(x, y) * value));
                    }
                    break;
            }
        } catch (RasterBufferInvalidException e) {
            throw new GridException("Buffer de datos no v�lido", e);
        } catch (OutOfGridException e1) {
            throw new GridException("Acceso fuera de los l�mites del Grid", e1);
        } catch (RasterBufferInvalidAccessException e) {
            throw new GridException("Acceso al grid no v�lido", e);
        } catch (InterruptedException e) {
        }
        switchToInterpolationMethod(interp);
    }
