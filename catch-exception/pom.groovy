project(modelVersion: '4.0.0') {

    parent(groupId: 'eu.codearte.catch-exception', artifactId: 'catch-exception-parent', version: '2.0.0-ALPHA-1') {
        relativePath '..'
    }

    artifactId 'catch-exception'
    packaging 'takari-jar'

    description 'Catch and verify exceptions'

    build {
        plugins {
            plugin(groupId: 'io.takari.maven.plugins', artifactId: 'takari-lifecycle-plugin')
            plugin(groupId: 'com.mycila', artifactId: 'license-maven-plugin')
        }
    }

    dependencies {
        dependency(groupId: 'org.assertj', artifactId: 'assertj-core', optional: true)
        dependency(groupId: 'junit', artifactId: 'junit', optional: true)
    }
}
