    public void actionPerformed(ActionEvent action) {
        if (readAFile.isSelected()) {
            input.setEditable(false);
            String inputData = writeAndReadToFile.readFile("./inputData.txt", "");
            output.setText(translationInPolishNotation.inPolishNotation(inputData, false));
            input.setText(inputData);
            result.setText(Double.toString(translationInPolishNotation.solvePolishRecord(translationInPolishNotation.inPolishNotation(inputData, true))));
        }
        if (input.getText().length() > 0) {
            output.setText(translationInPolishNotation.inPolishNotation(input.getText(), false));
            result.setText(Double.toString(translationInPolishNotation.solvePolishRecord(translationInPolishNotation.inPolishNotation(input.getText(), true))));
        }
        if (writeToFile.isSelected()) {
            writeAndReadToFile.writeToFile("./result.txt", "Infix notation: " + input.getText() + " Polish notation: " + output.getText() + " Result: " + result.getText());
        }
    }
