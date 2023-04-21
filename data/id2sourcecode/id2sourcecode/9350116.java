    @SuppressWarnings("static-access")
    public static void main(String argv[]) {
        if (Configuration.loadFromFile("testserv.cfg.xml") != 0) {
            System.err.println("Configuration file not found or parse error");
            return;
        }
        Connection connection = null;
        Program p = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(Configuration.getURL(), Configuration.getUser(), Configuration.getPassword());
            connection.setTransactionIsolation(connection.TRANSACTION_READ_COMMITTED);
            System.out.println("URL: " + Configuration.getURL());
            System.out.println("Connection: " + connection);
            Problems.connection = connection;
            p = new Program(1, "const nmax=1000;\n" + "var mass:array[1..nmax]of integer;\n" + "i,j,m,n:integer;\n" + "begin\n" + "read(n,m);\n" + "for i:=1 to n do\n" + "mass[i]:=i;\n" + "i:=1;\n" + "while n>1 do begin\n" + "i:=(i+m-1) mod n;\n" + "if i=0 then i:=n;\n" + "for j:=i to n-1 do\n" + "mass[j]:=mass[j+1];\n" + "n:=n-1;\n" + "end;\n" + "writeln(mass[1]);\n" + "end.\n", Problems.getInstance().getProblemById(6));
            p.prepare();
            Compiler c = CompilerFactory.getInstance().getCompiler(p.lang);
            c.compile(p);
            System.err.println("success compilation");
            InputGenerator inputGenerator = new InputFromFileGenerator(p);
            OutputGenerator outputGenerator = new OutputFromFileGenerator(p);
            InputDataProcessor inputDataProcessor = new SimpleInputDataProcessor();
            OutputDataProcessor outputDataProcessor = new SimpleOutputDataProcessor();
            ProgramTester tester = new ProgramTester(inputGenerator, outputGenerator, inputDataProcessor, outputDataProcessor);
            tester.execute(p);
        } catch (ClassNotFoundException e) {
            System.err.println("Mysql lib not found: " + e);
        } catch (SQLException e) {
            System.err.println("SQL error occurs: " + e);
        } catch (CompilationInternalServerErrorException e) {
            System.err.println(ExitCodes.getMsg(ExitCodes.INTERNAL_ERROR));
            System.err.println("Error while program compilation: " + e.getMessage());
        } catch (TestingInternalServerErrorException e) {
            System.err.println(ExitCodes.getMsg(ExitCodes.INTERNAL_ERROR));
            System.err.println("Error while program testing: " + e.getMessage());
        } catch (CompilationErrorException e) {
            System.err.println(ExitCodes.getMsg(ExitCodes.COMPILATION_ERROR));
            System.err.println(e.getMessage());
            System.err.println("Compilation error: " + e.getMessage());
        } catch (CompilationTimeLimitExceededException e) {
            System.err.println(ExitCodes.getMsg(ExitCodes.COMPILATION_ERROR));
            System.err.println(e.getMessage());
            System.err.println("Compilation process is out of time");
        } catch (TestingTimeLimitExceededException e) {
            System.err.println(ExitCodes.getMsg(ExitCodes.TIME_OUT_ERROR));
            System.err.println(e.getMessage());
            System.err.println("Testing process is out of time");
        } catch (CanNotCreateTemporaryDirectoryException e) {
            System.err.println(ExitCodes.getMsg(ExitCodes.INTERNAL_ERROR));
            System.err.println("Error while program temp directory creation: " + e.getMessage());
        } catch (CanNotCreateTemporaryFileException e) {
            System.err.println(ExitCodes.getMsg(ExitCodes.INTERNAL_ERROR));
            System.err.println("Error while program temp file creation: " + e.getMessage());
        } catch (CanNotWriteFileException e) {
            System.err.println(ExitCodes.getMsg(ExitCodes.INTERNAL_ERROR));
            System.err.println("Error while program file writing: " + e.getMessage());
        } catch (UnsuccessException e) {
            System.err.println(ExitCodes.getMsg(ExitCodes.UNSUCCESS));
            System.err.println("Failed tests: " + e.getMessage());
        } catch (RunTimeErrorException e) {
            System.err.println(ExitCodes.getMsg(ExitCodes.RUNTIME_ERROR));
            System.err.println(e.getMessage());
            System.err.println("Failed tests: " + e.getMessage());
        } finally {
            if (p != null) {
                p.close();
            }
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error while closing connection: " + e);
            }
        }
    }
