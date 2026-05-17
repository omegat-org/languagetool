plugins {
    `java-library`
    `maven-publish`
    jacoco
    signing
}

val projectGroup: String by project
val projectVersion: String by project

group = projectGroup
version = projectVersion

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
        vendor = JvmVendorSpec.ADOPTIUM
    }
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications.create<MavenPublication>("mavenJava") {
        from(components["java"])
        pom {
            name.set((project.findProperty("pomName") as String?) ?: project.name)
            description.set("languagetool library")
            url.set("https://github.com/omegat-org/languagetool/")
            licenses {
                license {
                    name.set("GNU Lesser General Public License, Version 2.1 or later (LGPL-2.1+)")
                    url.set("https://www.gnu.org/licenses/lgpl-2.1.html")
                    distribution.set("repo")
                }
            }

            developers {
                developer {
                    id.set("miurahr")
                    name.set("Hiroshi Miura")
                    email.set("miurahr@linux.com")
                }
            }

            scm {
                connection.set("scm:git:git://github.com/omegat-org/languagetool.git")
                developerConnection.set("scm:git:ssh://github.com/omegat-org/languagetool.git")
                url.set("https://github.com/omegat-org/languagetool/")
            }
        }
    }
}

signing {
    if (project.hasProperty("signingKey")) {
        val signingKey: String? by project
        val signingPassword: String? by project
        useInMemoryPgpKeys(signingKey, signingPassword)
    } else {
        useGpgCmd()
    }
    sign(publishing.publications["mavenJava"])
}

tasks.withType<Sign> {
    val hasKey = project.hasProperty("signingKey") || project.hasProperty("signing.gnupg.keyName")
    onlyIf { hasKey && !project.version.toString().endsWith("-SNAPSHOT") }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
    options.compilerArgs = listOf("-Xlint:none")
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
    setFailOnError(false)
    (options as StandardJavadocDocletOptions).addBooleanOption("Xdoclint:none", true)
    (options as StandardJavadocDocletOptions).addStringOption("Xmaxwarns", "1")
}

tasks.withType<Test> {
    minHeapSize = "512m"
    maxHeapSize = "1024m"
    jvmArgs = listOf("-XX:MaxMetaspaceSize=512m")
}

tasks.jacocoTestReport {
    reports {
        xml.required = false
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
    dependsOn(tasks.test)
}
