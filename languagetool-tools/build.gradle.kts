plugins {
    id("org.languagetool.java-conventions")
    `application`
}

application {
    mainClass = "org.languagetool.tools.SpellDictionaryBuilder"
}

dependencies {
    implementation(libs.jetbrains.annotations)
    implementation(project(":languagetool-core"))
    implementation(libs.commons.cli)
    implementation(libs.morfologik.tools)
    testImplementation(libs.junit4)
    testImplementation(project(":languagetool-core"))
    testImplementation(libs.logback.classic)
}

setProperty("pomName", "LanguageTool tools for building dictionaries")
description = "Developer tools for building LanguageTool dictionaries"
