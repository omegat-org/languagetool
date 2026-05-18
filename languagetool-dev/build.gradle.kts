plugins {
    id("org.languagetool.java-conventions")
}

dependencies {
    implementation(project(":languagetool-core"))
    implementation(project(":languagetool-commandline"))
    implementation(project(":languagetool-wikipedia"))
    implementation(project(":language-all"))
    implementation(libs.jetbrains.annotations)
    implementation(libs.lucene.core)
    implementation(libs.lucene.analyzers.common)
    implementation(libs.lucene.queries)
    implementation(libs.lucene.sandbox)
    implementation(libs.language.detector)
    implementation(libs.jackson.databind)
    implementation(libs.commons.compress)
    implementation(libs.commons.csv)
    implementation(libs.commons.cli)
    implementation(libs.commons.lang)
    implementation(libs.commons.io)
    implementation(libs.tukaani.xz)
    implementation(libs.morfologik.stemming)
    implementation(libs.mariadb)
    implementation(libs.guava)
    implementation(libs.jwordsplitter)
    runtimeOnly(libs.logback.classic)
    testImplementation(libs.junit4)
    testImplementation(testFixtures(project(":languagetool-core")))
    testImplementation(libs.lucene.test.framework)
    testRuntimeOnly(libs.logback.classic)
}

description = "LanguageTool wikipedia tools"
