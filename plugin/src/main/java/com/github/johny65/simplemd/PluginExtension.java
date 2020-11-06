package com.github.johny65.simplemd;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.provider.Property;

/**
 *
 * @author johny
 */
public abstract class PluginExtension {
    
    public abstract Property<Collection<Object>> getSources();

    public abstract Property<Object> getSource();
    
    public abstract Property<Object> getOutputDir();
    
    public File getOutputDir(Project project) {
        return project.file(getOutputDir().get());
    }

    public Collection<File> getInputFiles(Project project) {
        Collection<File> inputSources = new HashSet<>();
        
        //individual file or directory of files
        if (getSource().isPresent()) {
            File input = project.file(getSource().get());
            inputSources.addAll(analyzeInput(input));
        }
        
        //collection of files or directories
        if (getSources().isPresent()) {
            getSources().get().stream().map(project::file).forEach(f -> inputSources.addAll(analyzeInput(f)));
        }
        
        return inputSources;
    }
    
    private Collection<File> analyzeInput(File input) {
        if (input.exists()) {
            if (input.isDirectory()) {
                //scan directory for Markdown files
                return Stream.of(input.listFiles()).filter(this::isMarkdownFile).collect(Collectors.toSet());
            } else if (isMarkdownFile(input)) {
                return Set.of(input);
            } else {
                return Collections.emptySet();
            }
        } else{
            throw new GradleException(String.format("The input source %s does not exists.", input));
        }
    }
    
    private boolean isMarkdownFile(File file) {
        return file.isFile() && (file.getName().toLowerCase().endsWith(".md") ||
                file.getName().toLowerCase().endsWith(".markdown"));
    }    
}
