    public void addToCellValue(int x, int y, float value) throws GridException, InterruptedException {
        try {
            writer.setCellValue(x, y, (float) (reader.getCellValueAsFloat(x, y) + value));
        } catch (OutOfGridException e) {
            throw new GridException("");
        } catch (RasterBufferInvalidAccessException e) {
            throw new GridException("");
        } catch (RasterBufferInvalidException e) {
            throw new GridException("");
        }
    }
