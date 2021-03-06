/*
 * Copyright 2009-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.groovy.eclipse.core.launchers;

import org.codehaus.groovy.eclipse.core.compiler.CompilerUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.groovy.core.util.ArrayUtils;
import org.eclipse.jdt.launching.JavaLaunchDelegate;

public class GroovyShellLaunchDelegate extends JavaLaunchDelegate {

    @Override
    public String[] getClasspath(ILaunchConfiguration configuration) throws CoreException {
        String[] classpath = super.getClasspath(configuration);

        // check for required supporting libraries
        boolean jline = false, commons_cli = false;
        for (String entry : classpath) {
            if (entry.matches(".*\\bjline.*\\.jar$")) {
                jline = true;
            } else if (entry.matches(".*\\bcommons-cli.*\\.jar$")) {
                commons_cli = true;
            }
        }

        // try to add them to the classpath if they are not present
        if (!jline) {
            IPath path = CompilerUtils.getJarInGroovyLib("jline-*.jar");
            if (path != null) {
                classpath = (String[]) ArrayUtils.add(classpath, path.toOSString());
            }
        }
        if (!commons_cli) {
            IPath path = CompilerUtils.getJarInGroovyLib("commons-cli-*.jar");
            if (path != null) {
                classpath = (String[]) ArrayUtils.add(classpath, path.toOSString());
            }
        }

        return classpath;
    }
}
