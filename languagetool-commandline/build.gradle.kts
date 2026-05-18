plugins {
    id("org.languagetool.java-conventions")
}

dependencies {
    implementation(project(":languagetool-core"))
    implementation(libs.jetbrains.annotations)
    implementation(libs.commons.io)
    implementation(project(":language-en"))
    implementation(libs.lucene.core)
    runtimeOnly(project(":language-all"))
    testImplementation(libs.junit4)
    testImplementation(libs.commons.text)
    testImplementation(project(":language-en"))
    testImplementation(testFixtures(project(":languagetool-core")))
    testImplementation(libs.logback.classic)
}

description = "LanguageTool command-line version"
