    public void add(Grid g) throws GridException {
        if (g.getGridExtent().equals(getGridExtent())) {
            boolean interp = (reader instanceof GridInterpolated);
            switchToInterpolationMethod(false);
            try {
                switch(rasterBuf.getDataType()) {
                    case IBuffer.TYPE_BYTE:
                        for (int x = 0; x < g.getNX(); x++) {
                            for (int y = 0; y < g.getNY(); y++) {
                                writer.setCellValue(x, y, reader.getCellValueAsByte(x, y) + g.getCellValueAsByte(x, y));
                            }
                        }
                        break;
                    case IBuffer.TYPE_SHORT:
                        for (int x = 0; x < g.getNX(); x++) {
                            for (int y = 0; y < g.getNY(); y++) {
                                writer.setCellValue(x, y, reader.getCellValueAsShort(x, y) + g.getCellValueAsShort(x, y));
                            }
                        }
                        break;
                    case IBuffer.TYPE_INT:
                        for (int x = 0; x < g.getNX(); x++) {
                            for (int y = 0; y < g.getNY(); y++) {
                                writer.setCellValue(x, y, reader.getCellValueAsInt(x, y) + g.getCellValueAsInt(x, y));
                            }
                        }
                        break;
                    case IBuffer.TYPE_FLOAT:
                        for (int x = 0; x < g.getNX(); x++) {
                            for (int y = 0; y < g.getNY(); y++) {
                                writer.setCellValue(x, y, reader.getCellValueAsFloat(x, y) + g.getCellValueAsFloat(x, y));
                            }
                        }
                        break;
                    case IBuffer.TYPE_DOUBLE:
                        for (int x = 0; x < g.getNX(); x++) {
                            for (int y = 0; y < g.getNY(); y++) {
                                writer.setCellValue(x, y, reader.getCellValueAsDouble(x, y) + g.getCellValueAsDouble(x, y));
                            }
                        }
                        break;
                }
            } catch (OutOfGridException e) {
                throw new GridException("");
            } catch (RasterBufferInvalidAccessException e1) {
                throw new GridException("");
            } catch (RasterBufferInvalidException e) {
                throw new GridException("");
            } catch (InterruptedException e) {
            }
            switchToInterpolationMethod(interp);
        }
    }
