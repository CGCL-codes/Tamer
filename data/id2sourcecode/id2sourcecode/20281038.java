    public static FileStorageModel create(FileSystem root, String template) {
        if (template.startsWith("/")) template = template.substring(1);
        String[] s = template.split("%");
        char[] valid_formatCodes = new char[] { 'Y', 'y', 'j', 'm', 'd', 'H', 'M', 'S', 'v', 'V', 'x', 'b' };
        String[] formatName = new String[] { "Year", "2-digit-year", "day-of-year", "month", "day", "Hour", "Minute", "Second", "version", "Version", "date", "month-name" };
        int[] formatCode_lengths = new int[] { 4, 2, 3, 2, 2, 2, 2, 2, -1, -1, -1, -1 };
        int[] formatDigit = new int[] { StartYear4, StartYear2, StartDoy, StartMonth, StartDay, StartHour, StartMinute, StartSecond, Ignore, Ignore, Ignore, StartMonthName };
        int n = s.length;
        StringBuffer regex = new StringBuffer(100);
        regex.append(s[0]);
        int[] positions = new int[20];
        positions[0] = 0;
        int[] dateFormat = new int[n - 1];
        int[] p = new int[20];
        p[0] = 0;
        p[1] = s[0].length();
        boolean versioning = false;
        for (int i = 1; i < n; i++) {
            char firstChar = s[i].charAt(0);
            int len = -1;
            char fc = s[i].charAt(0);
            int index = -1;
            for (int j = 0; j < valid_formatCodes.length; j++) if (valid_formatCodes[j] == fc) index = j;
            String cc = s[i].substring(1);
            if (index == -1) {
                throw new IllegalArgumentException("invalid format code: " + fc);
            } else {
                String fieldName = formatName[index];
            }
            if (len == -1) len = formatCode_lengths[index];
            if (len == -1 && cc.equals("") && i < n - 1) {
                throw new IllegalArgumentException("invalid variable specification, need non-null constant string to delineate");
            }
            if (fc == 'v' || fc == 'V') versioning = true;
            String dots = ".........";
            regex.append("(" + dots.substring(0, len) + ")");
            regex.append(cc);
            dateFormat[i - 1] = formatDigit[index];
            p[i + 1] = p[i] + s[i].length() + 1;
            positions[i] = p[i + 1] - cc.length();
        }
        return FileStorageModel.create(root, regex.toString(), dateFormat);
    }
