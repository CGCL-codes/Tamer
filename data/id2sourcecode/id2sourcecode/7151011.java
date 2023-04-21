    public void addToCellValue(int x, int y, double value) throws GridException, InterruptedException {
        try {
            writer.setCellValue(x, y, (double) (reader.getCellValueAsDouble(x, y) + value));
        } catch (OutOfGridException e) {
            throw new GridException("");
        } catch (RasterBufferInvalidAccessException e) {
            throw new GridException("");
        } catch (RasterBufferInvalidException e) {
            throw new GridException("");
        }
    }
