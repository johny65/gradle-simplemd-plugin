package com.github.johny65.simplemd;

import org.gradle.api.Project;
import org.gradle.api.Plugin;

/**
 * 
 * @author johny
 */
public class GradleSimpleMDPlugin implements Plugin<Project> {

    private static final String TASK_NAME = "md2html";
    
    private static final String EXTENSION_NAME = "markdownConvertion";
    
    @Override
    public void apply(Project project) {
        var extension = project.getExtensions().create(EXTENSION_NAME, PluginExtension.class);
        project.getTasks().register(TASK_NAME, ConvertMarkdownTask.class, task -> {
            task.getSources().set(extension.getInputFiles(project));
            task.getOutputDir().set(extension.getOutputDir(project));
        });
    }
    
}
