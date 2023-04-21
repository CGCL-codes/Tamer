    public void addToCellValue(int x, int y, short value) throws GridException, InterruptedException {
        try {
            writer.setCellValue(x, y, (short) (reader.getCellValueAsShort(x, y) + value));
        } catch (OutOfGridException e) {
            throw new GridException("");
        } catch (RasterBufferInvalidAccessException e) {
            throw new GridException("");
        } catch (RasterBufferInvalidException e) {
            throw new GridException("");
        }
    }
