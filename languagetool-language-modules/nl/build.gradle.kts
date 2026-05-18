plugins {
    id("org.languagetool.java-conventions")
}

dependencies {
    implementation(libs.jetbrains.annotations)
    implementation(project(":languagetool-core"))
    implementation(libs.dutch.pos.dict)
    implementation(libs.guava)
    implementation(libs.trove4j)
    implementation(libs.indriya)
    implementation(libs.hankcs)
    implementation(libs.fastutil)
    implementation(libs.commons.lang)
    implementation(libs.morfologik.stemming)
    testImplementation(libs.junit4)
    testImplementation(project(":languagetool-core"))
    testImplementation(testFixtures(project(":languagetool-core")))
    testImplementation(libs.logback.classic)
}

description = "Dutch module for LanguageTool"
