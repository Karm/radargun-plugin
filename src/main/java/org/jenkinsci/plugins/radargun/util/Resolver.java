package org.jenkinsci.plugins.radargun.util;

import hudson.EnvVars;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.util.LogTaskListener;
import hudson.util.VariableResolver;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Convenient class for resolving/expanding various variabales.
 * 
 * @author vjuranek
 * 
 */
public class Resolver {
    
    private final AbstractBuild<?, ?> build;
    
    public Resolver(final AbstractBuild<?, ?> build) {
        this.build = build;
    }
    
    public String doResolve(final String toResolve) {
        return buildVar(build, toResolve);
    }

    public static String buildVar(final AbstractBuild<?, ?> build,final String toResolve) {
        if(toResolve == null)
            return null;
        
        VariableResolver<String> vr = build.getBuildVariableResolver();
        String resolved = Util.replaceMacro(toResolve, vr);
        try {
            EnvVars env = build.getEnvironment(new LogTaskListener(LOG, Level.INFO));
            resolved = env.expand(resolved);
        } catch (Exception e) {
            //TODO no-op?
        }
        return resolved;
    }
    
    private static final Logger LOG = Logger.getLogger(Resolver.class.getName());
}
