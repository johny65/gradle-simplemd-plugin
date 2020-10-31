package com.github.johny65.simplemd;

import java.io.File;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

/**
 *
 * @author johny
 */
public abstract class ConvertMarkdownTask extends DefaultTask {
    
    @Input
    public abstract Property<String> getSources();
    
    @OutputDirectory
    public abstract Property<String> getOutputDir();
    
    @TaskAction
    public void convert() {
        File input = new File(getSources().get());
        if (input.exists()) {
            if (input.isDirectory()) {
                System.out.println("The input is a directory, scan all .md files...");
            } else {
                System.out.println("The input is the file " + input);
            }
        } else{
            throw new GradleException("The input source does not exists.");
        }
        
        File output = new File(getOutputDir().get());
        if (output.exists()) {
            if (!output.isDirectory()) {
                throw new GradleException("The output is not a directory.");
            }
        } else {
            output.mkdir();
        }
        System.out.println("Output dir is " + output);
    }
    
}
