    public void execute(Reader reader, Writer writer, String conversionType, Properties java2HtmlConfig) throws IOException {
        JavaSource javaSource = null;
        javaSource = new JavaSourceParser().parse(reader);
        ArgumentValidation argVal = new ArgumentValidationImpl();
        argVal.isValidWhenInSet("conversionType", conversionType, conversionSet);
        if (argVal.containsIllegalArgument()) throw argVal.createIllegalArgumentException();
        IJavaSourceConverter converter = JavaSourceConverterProvider.getJavaSourceConverterByName(conversionType);
        converter.convert(javaSource, getConversionOption(), writer);
    }
