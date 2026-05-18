plugins {
    id("org.languagetool.java-conventions")
    application
}

application {
    mainClass = "org.languagetool.server.HTTPServer"
}

dependencies {
    api(libs.slf4j.api)
    implementation(project(":language-all"))
    implementation(project(":languagetool-core"))
    implementation(project(":languagetool-gui-commons"))
    implementation(libs.jetbrains.annotations)
    implementation(libs.bcrypt)
    implementation(libs.logback.classic)
    implementation(libs.logback.ecs.encoder)
    implementation(libs.jackson.databind)
    implementation(libs.commons.cli)
    implementation(libs.commons.codec)
    implementation(libs.opentelemetry.api)
    implementation(libs.opentelemetry.semconv)
    implementation(libs.grpc.stub)
    implementation(libs.grpc.netty.shaded)
    implementation(libs.grpc.protobuf)
    implementation(libs.commons.lang)
    implementation(libs.commons.pool)
    implementation(libs.guava)
    implementation(libs.lettuce)
    implementation(libs.prometheus.simpleclient.hotspot)
    implementation(libs.prometheus.simpleclient.httpserver)
    implementation(libs.prometheus.simpleclient.guava)
    implementation(libs.mariadb)
    implementation(libs.mybatis)
    testImplementation(libs.junit4)
    testImplementation(project(":languagetool-core"))
    testImplementation(libs.rest.assured)
    testImplementation(libs.hsqldb)
    testImplementation(libs.mockito.core)
    testImplementation(libs.okhttp)
    testImplementation(project(":language-en"))
    testImplementation(project(":language-de"))
    testImplementation(project(":language-ro"))
    testImplementation(project(":language-pl"))
    testImplementation(testFixtures(project(":languagetool-core")))
    testImplementation(libs.logback.classic)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
}

description = "LanguageTool embedded HTTP server"

tasks.withType<Test> {
    onlyIf {
        project.hasProperty("serverTests")
    }
}
