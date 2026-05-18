plugins {
    id("org.languagetool.java-conventions")
}

dependencies {
    implementation(project(":languagetool-core"))
    implementation(libs.jetbrains.annotations)
    implementation(libs.commons.cli)
    implementation(libs.commons.codec)
    implementation(libs.commons.compress)
    implementation(libs.commons.io)
    implementation(libs.lucene.core)
    implementation(libs.lucene.analyzers.common)
    implementation(libs.lucene.queries)
    implementation(libs.lucene.sandbox)
    implementation(libs.swc.engine)
    implementation(libs.swc.parser.lazy)
    implementation(libs.tukaani.xz)
    implementation(libs.morfologik.stemming)
    runtimeOnly(libs.logback.classic)
    runtimeOnly(project(":language-all"))
    testImplementation(libs.junit4)
    testImplementation(testFixtures(project(":languagetool-core")))
    testImplementation(libs.lucene.test.framework)
    testRuntimeOnly(libs.logback.classic)
    testImplementation(project(":language-de"))
    testImplementation(project(":language-en"))
}

description = "LanguageTool wikipedia tools"
