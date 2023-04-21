/*
 * EntityClassLoaders.java
 *
 * Created on July 23, 2006, 4:46 PM
 *
 * Copyright (c) Joerg Wassmer
 * This library is free software. You can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 2 or above
 * as published by the Free Software Foundation.
 * For more information please visit <http://jaxlib.sourceforge.net>.
 */

package jaxlib.persistence;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import jaxlib.io.file.DirectoryIterator;
import jaxlib.io.file.DirectoryIteratorEntry;
import jaxlib.io.file.DirectoryIteratorOption;
import jaxlib.io.file.Files;
import jaxlib.lang.classpath.Classpath;
import jaxlib.net.URLs;
import jaxlib.util.CheckArg;


/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: EntityClassLoaders.java 3018 2011-11-30 04:45:29Z joerg_wassmer $
 */
public class EntityClassLoaders extends Object
{

  protected EntityClassLoaders() throws InstantiationException
  {
    throw new InstantiationException();
  }


  public static Set<Class> loadEntitiesIgnoreDescriptor(
    final ClassLoader classLoader,
    final Classpath   classpath,
    final Matcher     classnameMatcher
  ) throws ClassNotFoundException, IOException, IllegalClassFormatException
  {
    CheckArg.notNull(classpath, "classpath");
    Set<Class> a = classpath.loadClasses(classLoader, classnameMatcher, Entity.class);
    a.addAll(classpath.loadClasses(classLoader, classnameMatcher, MappedSuperclass.class));
    return a;
  }



  public static Set<Class> loadByScanningClasspath(
    final ClassLoader classLoader,
    final URL         baseUrl,
    final Matcher     classNameMatcher
  ) throws ClassNotFoundException, IOException
  {
    CheckArg.notNull(classLoader, "classLoader");
    CheckArg.notNull(baseUrl,     "baseUrl");

    if (URLs.isFileURL(baseUrl))
    {
      return loadByScanningDirectoryTree(classLoader, URLs.toFile(baseUrl), classNameMatcher);
    }
    else if ("jar".equals(baseUrl.getProtocol()))
    {
      final URLConnection urlcon = baseUrl.openConnection();
      if (urlcon instanceof JarURLConnection)
      {
        return loadByScanningJarFile(
          classLoader, ((JarURLConnection) urlcon).getJarFile(), classNameMatcher
        );
      }
      else
      {
        throw new IOException("jar-url did not return JarURLConnection: " + urlcon);
      }
    }
    else
    {
      throw new IllegalArgumentException(
        "Unsupported url protocol, 'file' or 'jar' protocol required: " + baseUrl
      );
    }
  }



  public static Set<Class> loadByScanningDirectoryTree(
    final ClassLoader classLoader,
    final File        baseFile,
    final Matcher     classNameMatcher
  ) throws ClassNotFoundException, IOException
  {
    CheckArg.notNull(classLoader, "classLoader");
    CheckArg.notNull(baseFile,    "baseFile");

    final Path basePath = baseFile.toPath().toAbsolutePath();
    final LinkedHashSet<Class> result = new LinkedHashSet<>(64);

    final StringBuilder sb = new StringBuilder();
    try (
      final DirectoryIterator it = Files.iterator(
        basePath,
        DirectoryIteratorOption.RECURSIVE,
        DirectoryIteratorOption.FOLLOW_LINKS
      )
    )
    {
      while (it.hasNext())
      {
        final DirectoryIteratorEntry e = it.next();
        if (e.getAttributes().isRegularFile())
        {
          final String fname = e.getPath().getFileName().toString();
          if (fname.endsWith(".class") && !"package-info.class".equals(fname))
          {
            sb.setLength(0);
            Path p = e.getPath().getParent();
            while (true)
            {
              if (p == null)
                throw new IOException("File tree modified while scanning: " + baseFile);
              p = p.toAbsolutePath();
              if (basePath.equals(p))
                break;

              if (sb.length() > 0)
                sb.insert(0, '.');
              sb.insert(0, p.getFileName());
              p = p.getParent();
            }

            if (sb.length() > 0)
              sb.append('.');
            sb.append(fname.substring(0, fname.lastIndexOf('.')));

            final String cname = sb.toString();
            if (classNameMatcher != null)
            {
              classNameMatcher.reset(cname);
              if (!classNameMatcher.matches())
                continue;
            }

            final Class<?> c = Class.forName(cname, false, classLoader);
            if (!Modifier.isAbstract(c.getModifiers()) && c.isAnnotationPresent(Entity.class))
              result.add(c);
          }
        }
      }
    }

    return result;
  }



  public static Set<Class> loadByScanningJarFile(
    final ClassLoader classLoader,
    final JarFile     jarFile,
    final Matcher     classNameMatcher
  ) throws ClassNotFoundException, IOException
  {
    CheckArg.notNull(classLoader, "classLoader");
    CheckArg.notNull(jarFile,     "jarFile");

    final LinkedHashSet<Class> result = new LinkedHashSet<>(64);

    for (final Enumeration<JarEntry> it = jarFile.entries(); it.hasMoreElements();)
    {
      final JarEntry jarEntry = it.nextElement();
      final String entryName = jarEntry.getName();
      if (
           entryName.endsWith(".class")
        && !entryName.endsWith("package-info.class")
      )
      {
        final String cname = entryName.substring(0, entryName.lastIndexOf('.')).replace('/', '.');
        if (classNameMatcher != null)
        {
          classNameMatcher.reset(cname);
          if (!classNameMatcher.matches())
            continue;
        }

        final Class<?> c = Class.forName(cname, false, classLoader);
        if (!Modifier.isAbstract(c.getModifiers()) && c.isAnnotationPresent(Entity.class))
          result.add(c);
      }
    }

    return result;
  }

}
