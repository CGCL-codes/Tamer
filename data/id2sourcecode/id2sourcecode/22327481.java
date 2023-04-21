        @Override
        public String getTargetFileImpl(File source) {
            StringBuilder builder = new StringBuilder();
            builder.append(source.getParentFile().getAbsolutePath());
            if (builder.charAt(builder.length() - 1) != File.separatorChar) builder.append(File.separatorChar);
            if (prefix != null) builder.append(prefix);
            builder.append(source.getName());
            if (suffix != null) builder.append(suffix);
            File target = new File(builder.toString());
            if (!isFailOnOverwrite() && target.exists()) throw new RuntimeException(target + " already exists! source=" + source);
            return target.getAbsolutePath();
        }
