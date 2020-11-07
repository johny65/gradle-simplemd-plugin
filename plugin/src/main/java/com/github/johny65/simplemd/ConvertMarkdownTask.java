package com.github.johny65.simplemd;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

/**
 *
 * @author johny
 */
public abstract class ConvertMarkdownTask extends DefaultTask {
    
    private static final Logger LOGGER = Logger.getLogger(ConvertMarkdownTask.class.getName());
    
    private File outputDir;

    
    @InputFiles
    public abstract Property<Collection<File>> getSources();
    
    @OutputDirectory
    public abstract Property<File> getOutputDir();    
    
    @TaskAction
    public void taskAction() {
        System.out.println("Output: " + getOutputDir().get());
        getSources().get().forEach(f -> System.out.println("Input: " + f));
        this.outputDir = getOutputDir().get();
        getSources().get().forEach(this::processMd);
    }
    
    private void processMd(File markdownFile) {
        String fileName = markdownFile.getName();
        fileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".html";
        
        Path outputFilePath = outputDir.toPath().resolve(fileName);
        System.out.println("Output HTML file: " + outputFilePath);
        
        try {
            String markdown = Files.readString(markdownFile.toPath());
            String html = renderCommonMark(markdown);
            Files.writeString(outputFilePath, html);
        } catch (IOException ex) {
            throw new GradleException("Error converting MD", ex);
        }
    }
    
    /**
     * Renders the Markdown text (CommonMark dialect) into HTML.
     * 
     * @param markdownText
     * @return 
     */
    private String renderCommonMark(String markdownText) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdownText);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
    
    private void activateLogger() {
        deactivateLogger();
        Handler gradleConsoleHandler = new ConsoleHandler();
        gradleConsoleHandler.setLevel(Level.ALL);
        LOGGER.addHandler(gradleConsoleHandler);
        LOGGER.setUseParentHandlers(false);
        LOGGER.setLevel(Level.FINER);
    }
    
    private void deactivateLogger() {
        if (LOGGER.getHandlers() != null) {
            Stream.of(LOGGER.getHandlers()).forEach(LOGGER::removeHandler);
        }
    }
    
}
