    private String getMoveNumberBlack(int i) {
        int number = (i + 3) / 2;
        return (i % 2 == 0) ? Integer.toString(number) + "..." : "";
    }
