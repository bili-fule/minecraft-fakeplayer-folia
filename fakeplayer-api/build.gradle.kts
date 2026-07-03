plugins {
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.21"
}
dependencies {
    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
    }


    dependencies {
        paperweight.paperDevBundle("26.1.2.build.72-stable")
    }
}