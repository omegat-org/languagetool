plugins {
    `java-library`
    `maven-publish`
    jacoco
    signing
}

repositories {
    mavenCentral()
}

val projectGroup: String by project
val projectVersion: String by project

group = projectGroup
version = projectVersion

extra["pomName"] = project.name

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
        vendor = JvmVendorSpec.ADOPTIUM
    }
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications.create<MavenPublication>("mavenJava") {
        from(components["java"])
        pom {
            name.set(provider { (project.findProperty("pomName") as String?) ?: project.name })
            description.set(provider { project.description ?: "LanguageTool library module: ${project.name}" })
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

val signKey = listOf("signingKey", "signing.keyId", "signing.gnupg.keyName").find {project.hasProperty(it)}
tasks.withType<Sign> {
    onlyIf { signKey != null && !project.version.toString().endsWith("-SNAPSHOT") }
}

signing {
    when (signKey) {
        "signingKey" -> {
            val signingKey: String? by project
            val signingPassword: String? by project
            useInMemoryPgpKeys(signingKey, signingPassword)
        }
        "signing.keyId" -> {
        }
        "signing.gnupg.keyName" -> {
            useGpgCmd()
        }
    }
    sign(publishing.publications["mavenJava"])
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
    options.compilerArgs = listOf("-Xlint:none")
    // options.compilerArgs = listOf("-XDenableSunApiLintControl", "-Xlint:all",  "-Werror", "-Xlint:-sunapi")
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
