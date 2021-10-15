pipeline {
  agent any
  
  tools {
    gradle 'Default'
  }
  
  stages {
    
    stage("test") {
      
      steps {
        echo 'Testing the application'
        sh './gradlew -v'
        sh './gradlew test'
        echo 'Application tested'
      }
    }
  }
}
