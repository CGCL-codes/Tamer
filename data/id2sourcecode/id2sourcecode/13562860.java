    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        int index1 = rnd.nextInt(10);
        int index2 = rnd.nextInt(10) + 10;
        int temp = nums[index1];
        jTextArea1.append("\nIndex1 = " + index1 + "(" + nums[index1] + "), Index2 = " + index2 + "(" + nums[index2] + ")\n");
        for (int i = index1; i < index2; i++) {
            nums[i] = nums[i + 1];
        }
        nums[index2] = temp;
        printNums();
    }
