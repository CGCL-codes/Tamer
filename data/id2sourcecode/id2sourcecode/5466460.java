        private void assertAccess(final boolean readAccess, final boolean writeAccess, final String codeFragment, final String variableDeclarations, final String variableName) throws Exception {
            assertAccess(readAccess, true, false, codeFragment, variableDeclarations, variableName);
            assertAccess(writeAccess, false, true, codeFragment, variableDeclarations, variableName);
        }
