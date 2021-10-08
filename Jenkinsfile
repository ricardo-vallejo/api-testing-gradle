pipeline {
  agent any
  
  stages {
    
    stage("test") {
      
      steps {
        echo 'Testing the application'
        withGradle() {
          sh './gradlew -v'
        }
        echo 'Application tested'
      }
    }
  }
}
