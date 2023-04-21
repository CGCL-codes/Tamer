    private static String showEffectSet() {
        StringBuilder sb = new StringBuilder();
        sb.append("Effect Set Information:\n");
        for (int i = 0; i < levelSize.length; i++) {
            sb.append("Level: ");
            sb.append(i);
            sb.append("\n");
            for (int j = 0; j < levelSize[i]; j++) {
                sb.append(elementData[i][j].getClass().toString());
                sb.append("\n");
                sb.append("read set: ");
                sb.append(elementData[i][j].readwriteset().arrs.toString());
                sb.append("\n");
                sb.append("write set: ");
                sb.append(elementData[i][j].readwriteset().arws.toString());
            }
            sb.append("\n\n");
        }
        return sb.toString();
    }
