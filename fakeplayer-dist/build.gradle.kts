plugins {
    id("java")
    id("com.gradleup.shadow") version "9.4.3"
}

group = "io.github.hello09x.fakeplayer"
version = rootProject.version

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(26))
    }
}

dependencies {
    implementation(project(":fakeplayer-core"))
    implementation(project(":fakeplayer-api"))

    implementation(project(":fakeplayer-v26_1"))
}

// ShadowJar 配置
tasks.shadowJar {
    archiveFileName.set("fakeplayer-${project.version}.jar")
    archiveBaseName.set("fakeplayer")
    archiveClassifier.set("")
    archiveVersion.set("")

    exclude("META-INF/*.SF")
    exclude("META-INF/*.DSA")
    exclude("META-INF/*.RSA")

    //minimize()

    mergeServiceFiles()
}

tasks.jar {
    enabled = false
    dependsOn(tasks.shadowJar)
}

tasks.withType<AbstractArchiveTask> {
    destinationDirectory.set(file("../target"))
}

tasks.register("copyToServers") {
    dependsOn(tasks.shadowJar)

    doLast {
        val jarFile = tasks.shadowJar.get().archiveFile.get().asFile
        val servers = listOf(
                "../server-1.20.1/plugins",
                "../server-1.20.2/plugins",
                "../server-1.20.6/plugins",
                "../server-1.21/plugins",
                "../server-1.21.1/plugins",
                "../server-1.21.3/plugins",
                "../server-1.21.4/plugins",
                "../server-1.21.5/plugins",
                "../server-1.21.6/plugins",
                "../server-1.21.7/plugins",
                "../server-1.21.8/plugins"
        )

        servers.forEach { serverDir ->
            val dir = file(serverDir)
            if (!dir.exists()) {
                dir.mkdirs()
                println("Created directory: $serverDir")
            }

            copy {
                from(jarFile)
                into(dir)
                rename { "fakeplayer.jar" }
            }
            println("Copied to: $serverDir/fakeplayer.jar")
        }
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
    finalizedBy(tasks.named("copyToServers"))
}