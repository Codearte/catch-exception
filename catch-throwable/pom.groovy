project(modelVersion: '4.0.0') {

    parent(groupId: 'eu.codearte.catch-exception', artifactId: 'catch-exception-parent', version: '2.0.0-SNAPSHOT') {
        relativePath '..'
    }

    artifactId 'catch-throwable'
    packaging 'takari-jar'

    description 'Catch throwables (not only exceptions)'

    build {
        plugins {
            plugin(groupId: 'io.takari.maven.plugins', artifactId: 'takari-lifecycle-plugin')
        }
    }

    dependencies {
        dependency(groupId: 'org.assertj', artifactId: 'assertj-core', optional: true)
        dependency(groupId: 'junit', artifactId: 'junit', optional: true)
    }
}
