
plugins {
    id("org.languagetool.java-conventions")
}

dependencies {
    implementation(libs.jetbrains.annotations)
    implementation(libs.jackson.databind)
    implementation(libs.french.pos.dict)
    implementation(libs.commons.lang)
    implementation(project(":languagetool-core"))
    testImplementation(libs.junit4)
    testImplementation(project(":languagetool-core"))
    testImplementation(testFixtures(project(":languagetool-core")))
    testImplementation(libs.logback.classic)
}

setProperty("pomName", "French module for LanguageTool")
description = "French module for LanguageTool"

tasks.withType<Test> {
    onlyIf {
        project.hasProperty("frTests")
    }
}
