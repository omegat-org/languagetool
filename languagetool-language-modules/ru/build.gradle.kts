plugins {
    id("org.languagetool.java-conventions")
}

dependencies {
    implementation(libs.jetbrains.annotations)
    implementation(libs.openregex) { exclude(module = "guava") }
    implementation(libs.guava)
    implementation(project(":languagetool-core"))
    implementation(libs.commons.lang)
    implementation(libs.hankcs)
    implementation(libs.trove4j)
    implementation(libs.fastutil)
    testImplementation(libs.junit4)
    testImplementation(project(":languagetool-core"))
    testImplementation(testFixtures(project(":languagetool-core")))
    testImplementation(libs.logback.classic)
}

description = "Russian module for LanguageTool"
