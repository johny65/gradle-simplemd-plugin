package com.github.johny65.simplemd;

import org.gradle.api.provider.Property;

/**
 *
 * @author johny
 */
public abstract class PluginExtension {
    
    public abstract Property<String> getSources();
    
    public abstract Property<String> getOutputDir();
    
}
