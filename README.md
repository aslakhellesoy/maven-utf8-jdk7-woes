# Maven on JDK 7 can't compile classes with UTF-8 characters in the name.

*UPDATE*

Found the solution at [Stack Overflow](http://stackoverflow.com/questions/3610013/file-listfiles-mangles-unicode-names-with-jdk-6-unicode-normalization-issues) and fixed it in [this commit](https://github.com/cucumber/cucumber-jvm/commit/1298878e8faabfe22305d086c1151898ac1d595c). Good work by David Kowis, Sébastien Le Callonnec on [#cucumber](http://irclogger.com/.cucumber/2012-10-04).

Any tips about how to work around that would be greatly appreciated so I can build [Cucumber-JVM](https://github.com/cucumber/cucumber-jvm) on JDK 7. (Its i18n support uses classes with such weird characters).

I have posted a ticket: [MCOMPILER-183](http://jira.codehaus.org/browse/MCOMPILER-183).

How to reproduce:

## Works on both JDK 6 and JDK 7

```
mkdir -p target/classes
javac -encoding UTF-8 -d target/classes src/main/java/com/aslakhellesoy/Æøå.java
```

## Works on JDK 6, but *not* on JDK 7

```
mvn -X clean compile
```

See `mvn-jdk6.log` and `mvn-jdk7.log` to see why.

It turns out that when running on JDK 7, Maven tells javac to compile a file named `com/aslakhellesoy/Æøå.java`. It should be `com/aslakhellesoy/Æøå.java`.

I also tried `mvn -Dfile.encoding=UTF-8 clean compile`, but that did not help.

## Digging deeper

Try this on both JDK 6 and JDK 7:

```
javac Ls.java && java Ls | hexdump
```

JDK 7 gives this:

```
0000000 c3 86 c3 b8 61 cc 8a 2e 6a 61 76 61 3a 20 74 72
0000010 75 65 0a                                       
0000013
```

JDK 6 gives this:

```
0000000 ae bf 8c 2e 6a 61 76 61 3a 20 74 72 75 65 0a   
000000f
```

Not the same!! Apparently it's consistent on Linux, but not on OS X.

Another thing to try on both JDKs:

```
javac Ls.java && java Ls hex
```

JDK 7 gives this:

```
-0000000000000003c793c479e3375d1959e899f
```

JDK 6 gives this:

```
-000000000000000003c793c473c5ad1959e899f
```

It's clear that this has nothing to do with the terminal. It's something on the Filesystem level or System encoding level.

## Versions:

JDK 7 `mvn --version`

```
Apache Maven 3.0.3 (r1075438; 2011-02-28 17:31:09+0000)
Maven home: /usr/share/maven
Java version: 1.7.0_06-ea, vendor: Oracle Corporation
Java home: /Library/Java/JavaVirtualMachines/1.7.0.jdk/Contents/Home/jre
Default locale: en_US, platform encoding: UTF-8
OS name: "mac os x", version: "10.7.4", arch: "x86_64", family: "mac"
```

JDK 6 `mvn --version`

```
Apache Maven 3.0.3 (r1075438; 2011-02-28 17:31:09+0000)
Maven home: /usr/share/maven
Java version: 1.6.0_33, vendor: Apple Inc.
Java home: /System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home
Default locale: en_US, platform encoding: MacRoman
OS name: "mac os x", version: "10.7.4", arch: "x86_64", family: "mac"
```

Maven

