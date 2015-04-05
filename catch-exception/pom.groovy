project(modelVersion: '4.0.0') {

    parent(groupId: 'eu.codearte.catch-exception', artifactId: 'catch-exception-parent', version: '2.0.0-SNAPSHOT') {
        relativePath '..'
    }

    artifactId 'catch-exception'

    description 'Catch and verify exceptions'

    build {
        plugins {
            plugin(groupId: 'com.mycila', artifactId: 'license-maven-plugin') {
                configuration {
                    header '${maven.multiModuleProjectDirectory}/src/etc/header.txt'
                }
            }
        }
    }

    dependencies {
        dependency(groupId: 'org.assertj', artifactId: 'assertj-core', optional: true)
        dependency(groupId: 'junit', artifactId: 'junit', optional: true)
    }
}
