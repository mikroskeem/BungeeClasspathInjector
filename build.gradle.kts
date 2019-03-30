plugins {
    java
    id("net.minecrell.licenser") version "0.4.1"
    id("net.minecrell.plugin-yml.bungee") version "0.3.0"
}

group = "eu.mikroskeem"
version = "0.0.1-SNAPSHOT"

val waterfallApiVersion = "1.13-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("io.github.waterfallmc:waterfall-api:$waterfallApiVersion")
}

license {
    header = rootProject.file("etc/HEADER")
    filter.include("**/*.java")
}

bungee {
    name = "BungeeClasspathInjector"
    author = "${listOf("mikroskeem")}"
    main = "eu.mikroskeem.bungeeclasspathinjector.BungeeClasspathInjector"
}