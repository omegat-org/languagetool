plugins {
    id("org.languagetool.java-conventions")
}

dependencies {
    implementation(project(":languagetool-core"))
    implementation(libs.jetbrains.annotations)
    implementation(libs.commons.lang)
    testImplementation(libs.junit4)
    testImplementation(testFixtures(project(":languagetool-core")))
    testImplementation(libs.logback.classic)
}

setProperty("pomName", "LanguageTool common GUI classes")
description = "GUI classes for both stand-alone and LibreOffice/OpenOffice extension use"