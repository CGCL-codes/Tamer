    public void addToCellValue(int x, int y, int value) throws GridException, InterruptedException {
        try {
            writer.setCellValue(x, y, (int) (reader.getCellValueAsInt(x, y) + value));
        } catch (OutOfGridException e) {
            throw new GridException("");
        } catch (RasterBufferInvalidAccessException e) {
            throw new GridException("");
        } catch (RasterBufferInvalidException e) {
            throw new GridException("");
        }
    }
