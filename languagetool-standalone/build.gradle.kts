plugins {
    id("org.languagetool.java-conventions")
}

dependencies {
    implementation(project(":languagetool-core"))
    implementation(project(":languagetool-commandline"))
    implementation(project(":languagetool-gui-commons"))
    implementation(project(":languagetool-server"))
    implementation(project(":languagetool-tools"))
    implementation(libs.jetbrains.annotations)
    implementation(libs.commons.collections4)
    implementation(libs.commons.io)
    implementation(libs.commons.lang)
    implementation(libs.lucene.core)
    implementation(project(":language-en"))
    runtimeOnly(project(":language-all"))
    testImplementation(libs.junit4)
    testImplementation(libs.mockito.core)
    testImplementation(libs.language.detector)
    testImplementation(project(":language-all"))
    testImplementation(testFixtures(project(":languagetool-core")))
    testImplementation(libs.logback.classic)
}

description = "LanguageTool stand-alone GUI"

tasks.withType<Test> {
    minHeapSize = "512m"
    maxHeapSize = "4096m"
    jvmArgs = listOf("-XX:MaxMetaspaceSize=512m")
}