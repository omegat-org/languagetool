plugins {
    id("org.languagetool.java-conventions")
}

dependencies {
    implementation(project(":languagetool-core"))
    implementation(libs.catalan.pos.dict)
    implementation(libs.jetbrains.annotations)
    implementation(libs.commons.lang)
    implementation(libs.guava)
    testImplementation(libs.junit4)
    testImplementation(project(":languagetool-core"))
    testImplementation(testFixtures(project(":languagetool-core")))
    testImplementation(libs.logback.classic)
}

description = "Catalan module for LanguageTool"

tasks.withType<Test> {
    onlyIf {
        project.hasProperty("caTests")
    }
}
