    private void generateSpreadsheet(final String symbol) throws IOException {
        LOGGER.debug("Generating Spreadsheet for " + symbol);
        final StockCollection collection = new StockCollection(symbol, true);
        final List<Stock> stocks = collection.getStocks();
        final SingleStockSpreadsheet sheet = new SingleStockSpreadsheet(stocks);
        sheet.writeOutSpreadsheet();
    }
