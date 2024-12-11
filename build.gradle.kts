import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.shadow)
    alias(libs.plugins.run.paper)
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
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

description = "A skript addon that aims to link Nexo and Skript"
group = "me.asleepp"
version = "1.0.5"

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(libs.lombok)
    compileOnly(libs.nexo)
    compileOnly(libs.skript)
    annotationProcessor(libs.lombok)
}


tasks {
    jar {
        enabled = false
    }

    shadowJar {
        archiveFileName = "${rootProject.name}-${project.version}.jar"
        archiveClassifier = null

        manifest {
            attributes["Implementation-Version"] = rootProject.version
        }

        configurations = listOf(project.configurations.shadow.get())
        minimize()
    }

    assemble {
        dependsOn(shadowJar)
    }

    withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release = 17
    }

    withType<Javadoc>() {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filter<ReplaceTokens>("tokens" to mapOf("version" to project.version))
        inputs.property("version", project.version)

        filesMatching("plugin.yml") {
            expand(
                "version" to rootProject.version,
            )
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
        runDirectory = rootDir.resolve("run/paper/$version")

        javaLauncher = project.javaToolchains.launcherFor {
            languageVersion = javaVersion
        }

        downloadPlugins {
            url("https://github.com/SkriptLang/Skript/releases/download/2.9.4/Skript-2.9.4.jar")
            // Add your own plugins to download here, stick any that you cant download into the run folder this generates.
        }

        jvmArgs = jvmArgsExternal
    }
}
