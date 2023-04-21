    public boolean ExportProjects(String filename, int[] list, boolean zip) {
        progress = new Progress("Exporting Projects", list.length);
        FileResource eFile = null;
        boolean result = true;
        try {
            progress.addMessageLine("validating target " + filename);
            String target_home = filename.substring(0, filename.lastIndexOf(File.separatorChar));
            if (filename == null || filename.trim().equals("")) {
                zip = true;
                progress.addMessageLine("creating export file " + filename);
                eFile = setExportFile(exportFileName);
                if (!eFile.create(true)) {
                    this.getError().setErrorString("Error exporting Projects : error creating file");
                    return false;
                }
            }
            if (zip) {
                if (!new File(filename).getParentFile().exists()) {
                    this.getError().setErrorString("Error exporting Projects : invalid location");
                    return false;
                }
            } else {
                if (!new File(filename).isDirectory()) {
                    this.getError().setErrorString("Error exporting Projects : invalid location");
                    return false;
                } else {
                    String can_map_context = new File(this.getHome()).getCanonicalPath();
                    String can_exp = new File(filename).getCanonicalPath();
                    if (can_exp.equals(can_map_context)) {
                        this.getError().setErrorString("Error exporting Projects : Cannot export to the current projects directory");
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            this.getError().setErrorString("Error exporting Projects : invalid location");
            System.out.println("Error exporting Projects : invalid location");
            e.printStackTrace();
            return false;
        }
        progress.addMessageLine("target valid");
        if (list.length <= 0) {
            this.getError().setErrorString("Error exporting Projects : no projects to export");
            System.out.println("Error exporting Projects : no projects to export");
            return false;
        }
        ZipOutputStream zos = null;
        try {
            progress.addMessageLine("preparing target");
            MapContext export_map_context = new MapContext(this.getHome(), false);
            String projects_zip = filename;
            if (zip) {
                if (!projects_zip.toLowerCase().endsWith(".zip")) {
                    projects_zip += ".zip";
                }
                try {
                    projects_zip = new File(projects_zip).getCanonicalPath().toString();
                } catch (Exception e) {
                }
                export_map_context = new MapContext(new File(projects_zip).getParent(), false);
                zos = new ZipOutputStream(new FileOutputStream(projects_zip));
            } else {
                export_map_context = new MapContext(filename, false);
            }
            boolean stopexport = false;
            String filef = new File(filename).getName();
            MapContext cloned_map_context = (MapContext) this.clone();
            for (int i = 0; i < list.length && !stopexport; i++) {
                String projectname = cloned_map_context.getMapFiles().at(list[i]).getResource();
                int project_index = list[i];
                progress.addProgressLine("exporting " + projectname);
                FileResource exported_file_resource = new FileResource(projectname, filef, new File(filename).getParent());
                String source_directory_map = cloned_map_context.getMapFiles().at(project_index).getHome() + File.separatorChar + FileResource.constructFilenameFromName(projectname) + File.separatorChar;
                if (zos != null) {
                    zos.putNextEntry(new ZipEntry(FileSystem.getRelativePathname(new File(cloned_map_context.getMapFiles().at(project_index).getHome()), new File(cloned_map_context.getMapAt(list[i]).getHome() + "map.xml"))));
                    XMLUtilOld.save(zos, (Map) cloned_map_context.getMapAt(list[i]).clone(), "Map");
                    int map_size = cloned_map_context.getMapAt(list[i]).getSize();
                    try {
                        String project_home = cloned_map_context.getMapFiles().at(project_index).getHome();
                        GroupLayer root_layer = (GroupLayer) cloned_map_context.getMapAt(list[i]);
                        result = rebuildProject(root_layer, project_home, zos);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return false;
                    }
                } else {
                    String target_directory_map = export_map_context.getHome() + File.separatorChar + FileResource.constructFilenameFromName(projectname) + File.separatorChar;
                    new File(target_directory_map).mkdirs();
                    FileSystem.copyDirectory(new File(source_directory_map), target_directory_map);
                }
                export_map_context.getMapFiles().add(exported_file_resource);
                export_map_context.translate_name_vec.addElement(cloned_map_context.translate_name_vec.elementAt(list[i]));
                export_map_context.map_id_vec.addElement(projectname);
                if (!progress.isRunning()) {
                    stopexport = true;
                }
            }
            if (true) {
                if (zos != null) {
                    zos.putNextEntry(new ZipEntry("projects.xml"));
                    XMLUtilOld.save(zos, export_map_context, "ExportedProjects");
                    File projects_zip_file = new File(projects_zip);
                    String projects_zip_filename = projects_zip_file.getName();
                    String projects_zip_parent = projects_zip_file.getParent();
                    FileResource fres = new FileResource(projects_zip_filename, projects_zip_filename, projects_zip_parent);
                    setExportFile(fres);
                    zos.closeEntry();
                    zos.close();
                } else {
                    export_map_context.save();
                }
            }
        } catch (Exception e) {
            this.getError().setErrorString("Error exporting Project");
            this.getError().addErrorString(" : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        progress.addMessageLine("export done");
        progress.stop();
        return result;
    }
