    public void addToCellValue(int x, int y, byte value) throws GridException, InterruptedException {
        try {
            writer.setCellValue(x, y, (byte) (reader.getCellValueAsByte(x, y) + value));
        } catch (OutOfGridException e) {
            throw new GridException("");
        } catch (RasterBufferInvalidAccessException e) {
            throw new GridException("");
        } catch (RasterBufferInvalidException e) {
            throw new GridException("");
        }
    }
