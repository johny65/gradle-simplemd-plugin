package com.github.johny65.simplemd;

import org.gradle.api.Project;
import org.gradle.api.Plugin;

/**
 * 
 * @author johny
 */
public class GradleSimpleMDPlugin implements Plugin<Project> {

    private static final String TASK_NAME = "md2html";
    
    @Override
    public void apply(Project project) {
        var extension = project.getExtensions().create(TASK_NAME, PluginExtension.class);
        
        project.getTasks().register(TASK_NAME, ConvertMarkdownTask.class, task -> {
            task.getSources().set(extension.getSources());
            task.getOutputDir().set(extension.getOutputDir());
        });
    }
    
}
