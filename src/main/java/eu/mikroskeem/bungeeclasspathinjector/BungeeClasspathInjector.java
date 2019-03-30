/*
 * This file is part of project BungeeClasspathInjector, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2019 Mark Vainomaa <mikroskeem@mikroskeem.eu>
 * Copyright (c) Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package eu.mikroskeem.bungeeclasspathinjector;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Mark Vainomaa
 */
public final class BungeeClasspathInjector extends Plugin {
    @Override
    public void onLoad() {
        File libsDirectory = new File(getDataFolder(), "libs");
        if(!libsDirectory.mkdirs() && !libsDirectory.exists()) {
            throw new RuntimeException("Failed to create directories for path: " + libsDirectory);
        }

        try {
            injectJars(libsDirectory.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to inject jars to classpath", e);
        }
    }

    private void injectJars(Path libsDirectory) throws IOException {
        Files.walk(libsDirectory).filter(f -> Files.isRegularFile(f) && f.getFileName().toString().endsWith(".jar")).forEach(jarFile -> {
            getLogger().info("Adding jar " + jarFile.getFileName() + " into classpath");

            try {
                addUrl(jarFile.toUri().toURL());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void addUrl(URL url) throws Exception {
        URLClassLoader classLoader = (URLClassLoader) ProxyServer.class.getClassLoader();
        addUrlMethod.invoke(classLoader, url);
    }

    private static Method addUrlMethod;

    static {
        try {
            addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addUrlMethod.setAccessible(true);
        } catch (Exception ignored) {}
    }
}
