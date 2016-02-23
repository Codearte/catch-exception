project(modelVersion: '4.0.0') {

  groupId 'eu.codearte.catch-exception'
  artifactId 'catch-exception-parent'
  version '2.0.0-SNAPSHOT'
  packaging 'pom'

  name 'catch-exception-project'
  description 'Catch and verify exceptions - parent module'
  url 'https://github.com/Codearte/catch-exception/'

  def javaVersion = '1.8'
  def assertJVersion = '3.3.0'

  licenses {
    license {
      name 'Apache 2'
      url 'http://www.apache.org/licenses/LICENSE-2.0.txt'
      distribution 'repo'
      comments 'A business-friendly OSS license'
    }
  }
  developers {
    developer(id: 'rwoo', name: 'Rod Woo', email: 'rwoo@gmx.de')
    developer(id: 'mariuszs', name: 'Mariusz Smykula', email: 'mariuszs@gmail.com')
  }
  prerequisites {
    maven '3.3.9'
  }
  modules {
    module 'catch-exception'
    module 'catch-throwable'
  }
  scm {
    connection 'scm:git:https://github.com/Codearte/catch-exception.git'
    developerConnection 'scm:git:git@github.com:Codearte/catch-exception.git'
    url 'https://github.com/Codearte/catch-exception/'
  }
  distributionManagement {
    repository(id: 'sonatype-nexus-staging', url: 'http://oss.sonatype.org/service/local/staging/deploy/maven2/')
    snapshotRepository(id: 'sonatype-nexus-snapshots', url: 'http://oss.sonatype.org/content/repositories/snapshots')
  }
  properties {
    'project.inceptionYear' '2011'
    'project.build.sourceEncoding' 'UTF-8'
    'maven.compiler.source' javaVersion
    'maven.compiler.target' javaVersion
  }
  dependencyManagement {
    dependencies {
      dependency(groupId: 'org.assertj', artifactId: 'assertj-core', version: assertJVersion, optional: true)
      dependency(groupId: 'junit', artifactId: 'junit', version: 4.12, optional: true)
    }
  }

  build {
    pluginManagement {
      plugins {
        plugin(groupId: 'io.takari.maven.plugins', artifactId: 'takari-lifecycle-plugin', version: '1.11.11', extensions: true) {
          configuration {
            sourceJar 'true'
            testJar 'true'
          }
        }
        plugin(artifactId: 'maven-clean-plugin', version: '2.6.1')
        plugin(artifactId: 'maven-javadoc-plugin', version: '2.10.3')
        plugin(artifactId: 'maven-site-plugin', version: '3.4')
        plugin(artifactId: 'maven-release-plugin', version: '2.5.3') {
          configuration {
            mavenExecutorId 'forked-path'
            pushChanges 'false'
            localCheckout 'true'
            autoVersionSubmodules 'true'
            tagNameFormat 'catch-exception-@{project.version}'
          }
        }
        plugin(artifactId: 'maven-gpg-plugin', version: 1.6) {
          executions {
            execution(id: 'sign-artifacts', phase: 'verify') {
              goals {
                goal 'sign'
              }
            }
          }
        }
        plugin(artifactId: 'maven-surefire-plugin', version: '2.18.1')
        plugin(groupId: 'org.eluder.coveralls', artifactId: 'coveralls-maven-plugin', version: '3.1.0')
        plugin(groupId: 'org.jacoco', artifactId: 'jacoco-maven-plugin', version: '0.7.5.201505241946')
      }
    }
    plugins {
      plugin(groupId: 'org.jacoco', artifactId: 'jacoco-maven-plugin') {
        executions {
          execution(id: 'prepare-agent') {
            goals {
              goal 'prepare-agent'
            }
          }
          execution(id: 'report') {
            goals {
              goal 'report'
            }
          }
        }
      }
      plugin(artifactId: 'maven-javadoc-plugin') {
        executions {
          execution(id: 'aggregate', phase: 'site') {
            goals {
              goal 'aggregate'
            }
          }
        }
        configuration {
          show 'private'
          overview '${maven.multiModuleProjectDirectory}/src/main/javadoc/overview/overview.html'
          linksource 'true'
          docfilessubdirs 'true'
        }
      }
      plugin(groupId: 'com.mycila', artifactId: 'license-maven-plugin', version: '2.11') {
        executions {
          execution {
            goals {
              goal 'check'
            }
          }
        }
        configuration {
          header 'src/etc/header.txt'
          includes {
            include 'src/**'
          }
          aggregate 'true'
          properties {
            year '${project.inceptionYear}'
            email 'rwoo@gmx.de'
          }
        }
      }
    }
  }
  profiles {
    profile(id: 'release') {
      activation {
        property(name: 'performRelease', value: 'true')
      }
      build {
        plugins {
          plugin(artifactId: 'maven-gpg-plugin')
        }
      }
    }
  }
}
