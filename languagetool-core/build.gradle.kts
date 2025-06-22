plugins {
    id("org.languagetool.java-conventions")
    `java-test-fixtures`
}

sourceSets {
    testFixtures {
        java {
            setSrcDirs(listOf("src/fixtures/java"))
        }
        resources {
            setSrcDirs(listOf("src/fixtures/resources"))
        }
    }
}

dependencies {
    api(libs.jaxb.api)
    api(libs.slf4j.api)
    implementation(libs.trove4j)
    implementation(libs.emoji.java)
    implementation(libs.hppc)
    implementation(libs.jackson.databind)
    implementation(libs.guava)
    implementation(libs.hankcs)
    implementation(libs.language.detector)
    implementation(libs.commons.logging)
    implementation(libs.commons.validator)
    implementation(libs.resilience4j.circuitbreaker)
    implementation(libs.resilience4j.micrometer)
    implementation(libs.opentelemetry.api)
    implementation(libs.opentelemetry.semconv)
    implementation(libs.grpc.stub)
    implementation(libs.grpc.netty.shaded)
    implementation(libs.grpc.protobuf)
    implementation(libs.micrometer.registory.prometheus)
    implementation(libs.prometheus.simpleclient)
    implementation(libs.prometheus.simpleclient.guava)
    implementation(libs.javax.activation.api)
    implementation(libs.javax.annotation.api)
    implementation(libs.javax.measure.unit)
    implementation(libs.loomchild.segment)
    implementation(libs.commons.lang)
    implementation(libs.commons.pool)
    implementation(libs.commons.text)
    implementation(libs.lucene.core)
    implementation(libs.lucene.analyzers.common)
    implementation(libs.lucene.backward.codecs)
    implementation(libs.morfologik.fsa)
    implementation(libs.morfologik.builders)
    implementation(libs.morfologik.speller)
    implementation(libs.morfologik.stemming)
    implementation(libs.jaxb.runtime)
    implementation(libs.jetbrains.annotations)
    implementation(libs.json)
    implementation(libs.indriya)
    implementation(libs.openregex) { exclude(module = "guava") }
    implementation(libs.guava)
    implementation(libs.java.diff.utils)
    implementation(libs.fastutil)
    testFixturesApi(project(":languagetool-core"))
    testFixturesApi(libs.junit4)
    testFixturesImplementation(libs.morfologik.stemming)
    testFixturesImplementation(libs.morfologik.builders)
    testFixturesImplementation(libs.jetbrains.annotations)
    testFixturesImplementation(libs.resilience4j.circuitbreaker)
    testFixturesImplementation(libs.resilience4j.micrometer)
    testImplementation(libs.logback.classic)
    testImplementation(libs.junit4)
    testImplementation(libs.awaitility)
    testImplementation(libs.mockito.core)
    testImplementation(testFixtures(project(":languagetool-core")))

    testFixturesCompileOnly(libs.lombok)
    testFixturesAnnotationProcessor(libs.lombok)

    compileOnly(libs.lombok)
	annotationProcessor(libs.lombok)

	testCompileOnly(libs.lombok)
	testAnnotationProcessor(libs.lombok)
}

description = "LanguageTool Style and Grammar Checker Core"
