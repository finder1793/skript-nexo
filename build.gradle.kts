import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.shadow)
    alias(libs.plugins.run.paper)
    kotlin("jvm")
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.skriptlang.org/releases")
    maven("https://repo.nexomc.com/snapshots/")
}

java {
    disableAutoTargetJvm()
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

description = "A skript addon that aims to link Nexo and Skript"
group = "me.asleepp"
version = "2.1.0"

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(libs.lombok)
    compileOnly(libs.nexo)
    compileOnly(libs.skript)
    implementation(libs.creative)
    annotationProcessor(libs.lombok)
    implementation(kotlin("stdlib-jdk8"))
}

tasks {
    jar {
        enabled = false
    }

    shadowJar {
        archiveFileName.set("${rootProject.name}-${project.version}.jar")
        archiveClassifier.set("")

        manifest {
            attributes["Implementation-Version"] = project.version
        }

        configurations = listOf(project.configurations.runtimeClasspath.get())
        minimize()
        relocate("team.unnamed.creative", "me.asleepp.skriptnexo.shadow.team.unnamed.creative")
    }

    assemble {
        dependsOn(shadowJar)
    }

    withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filter<ReplaceTokens>("tokens" to mapOf("version" to project.version))
        inputs.property("version", project.version)

        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }

    defaultTasks("build")

    // 1.8.8 - 1.16.5 = Java 8
    // 1.17           = Java 16
    // 1.18 - 1.20.4  = Java 17
    // 1-20.5+        = Java 21
    val version = "1.21.1"
    val javaVersion = JavaLanguageVersion.of(21)

    val jvmArgsExternal = listOf(
        "-Dcom.mojang.eula.agree=true"
    )

    runServer {
        minecraftVersion(version)
        runDirectory.set(rootDir.resolve("run/paper/$version"))

        javaLauncher.set(project.javaToolchains.launcherFor {
            languageVersion.set(javaVersion)
        })

        downloadPlugins {
            url("https://github.com/SkriptLang/Skript/releases/download/2.9.4/Skript-2.9.4.jar")
            // Add your own plugins to download here, stick any that you can't download into the run folder this generates.
        }

        jvmArgs = jvmArgsExternal
    }
}