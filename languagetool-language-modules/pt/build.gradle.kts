plugins {
    id("org.languagetool.java-conventions")
}
dependencies {
    implementation(libs.jetbrains.annotations)
    implementation(project(":languagetool-core"))
    implementation(libs.portuguese.pos.dict)
    implementation(libs.indriya)
    implementation(libs.hankcs)
    implementation(libs.commons.lang)
    testImplementation(libs.junit4)
    testImplementation(project(":languagetool-core"))
    testImplementation(testFixtures(project(":languagetool-core")))
    testImplementation(libs.logback.classic)
}

description = "Portuguese module for LanguageTool"
