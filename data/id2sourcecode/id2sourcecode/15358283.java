        @Override
        public String toString() {
            return new ToStringGenerator(this).append("class", m_aClass).append("field", m_sField).appendIfNotNull("readMethodName", m_sReadMethodName).appendIfNotNull("writeMethodName", m_sWriteMethodName).toString();
        }
