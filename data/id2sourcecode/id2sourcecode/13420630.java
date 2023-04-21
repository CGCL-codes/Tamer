    private void __setProgress(String s, int completed, int total) {
        if (terminal == null) return;
        int w = reader.getTermwidth();
        int progress = (completed * 20) / total;
        String totalStr = String.valueOf(total);
        String percent = String.format("%0" + totalStr.length() + "d/%s [", completed, totalStr);
        String result;
        if (s == null) result = percent + repeat("=", progress) + repeat(" ", 20 - progress) + "]"; else result = s + percent + repeat("=", progress) + repeat(" ", 20 - progress) + "]";
        try {
            reader.getCursorBuffer().clearBuffer();
            reader.getCursorBuffer().write(result);
            reader.setCursorPosition(w);
            reader.redrawLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
