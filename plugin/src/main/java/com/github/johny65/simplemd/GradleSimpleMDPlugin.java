package com.github.johny65.simplemd;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.gradle.api.GradleException;
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
            task.getSources().set(extension.getInputFiles(project));
            task.getOutputDir().set(extension.getOutputDir(project));

            task.getOutputs().upToDateWhen(t -> false);
        });
    }
    
    
}
