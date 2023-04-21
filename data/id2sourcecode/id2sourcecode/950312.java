    public static void main(String[] args) {
        int cases = Integer.parseInt(readLn1());
        for (int caseNumber = 0; caseNumber < cases; caseNumber++) {
            System.out.println("Case #" + (caseNumber + 1) + ":");
            String line = readLn();
            int k = (int) Math.sqrt(line.length());
            if (k * k != line.length()) System.out.print("No magic :("); else {
                if (!checkPalindrome(line)) {
                    System.out.print("No magic :(");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j < k; j++) {
                        for (int l = 0; l < k; l++) {
                            sb.append(line.charAt(l * k + j));
                        }
                    }
                    if (!sb.toString().equals(line)) {
                        System.out.print("No magic :(");
                    } else {
                        System.out.print(k);
                    }
                }
            }
            System.out.println();
        }
    }
