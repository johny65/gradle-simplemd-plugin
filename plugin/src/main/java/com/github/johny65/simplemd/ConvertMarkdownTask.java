package com.github.johny65.simplemd;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.OutputFiles;
import org.gradle.api.tasks.TaskAction;

/**
 *
 * @author johny
 */
public abstract class ConvertMarkdownTask extends DefaultTask {
    
    private static final Logger LOGGER = Logger.getLogger(ConvertMarkdownTask.class.getName());
    
    private Collection<File> inputSources;
    
    //private File outputDir;

    @InputFiles
    public abstract Property<Collection<File>> getSources();
    
    @OutputDirectory
    public abstract Property<File> getOutputDir();    
    
    @TaskAction
    public void taskAction() {
        System.out.println("Output: " + getOutputDir().get());
        getSources().get().forEach(f -> System.out.println("Input: " + f));
//        process();
    }
    
    
    private void process() {
        getSources().get().forEach(this::processMd);
    }

    private void processMd(File markdownFile) {
        String fileName = markdownFile.getName();
        fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "html";
        
//        File outputFile = outputDir.
//        outputFile.getParentFile().mkdirs();
//        try (FileOutputStream fos = new FileOutputStream(outputFile);
//                OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
//            writer.write(content);
//        }
        
//        try {
//            Path outputPath = convetToOutputPath(mdDir, outputDir, markdownPath);
//            StringBuilder markdownText = new StringBuilder();
//            Files.readAllLines(markdownPath).forEach(markdownText::append);
//
//            String fileName = getFileName(markdownPath);
//            String htmlBody = renderHtml(markdownPath, markdownText.toString());
//
//            output(outputPath, this.builder.replaceTitle(fileName).replaceBody(htmlBody).toString());
//        } catch (IOException e) {
//            throw new GradleException("execute error.", e);
//        }
    }

    private Path convetToOutputPath(Path inputRootPath, Path outputRootPath, Path inputFilePath) {
        String relativePath = inputFilePath.toString().substring(inputRootPath.toString().length() + 1);
        String filePath = relativePath.substring(0, relativePath.lastIndexOf(".")) + "html";
        return outputRootPath.resolve(filePath);
    }

    private String getFileName(Path path) {
        String fileName = path.getFileName().toString();
        int extensionIndex = fileName.lastIndexOf(".");
        return fileName.substring(0, extensionIndex);
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
    
    
    protected void output(Path outputPath, String content) throws IOException {
        
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
