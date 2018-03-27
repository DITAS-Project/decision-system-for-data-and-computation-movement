pipeline {
    // Mandatory to use per-stage agents
    agent none
    stages {
        stage('Build - test') {
            agent {
                dockerfile {
                    filename 'Dockerfile.build'
                }
            }
            steps {
                // The following command creates the  WAR inside the target folder in the workspace 
                sh 'mvn package'

                // Archive the artifact to be accessible from the Artifacts tab into the Blue Ocean interface, just to have it handy
                archiveArtifacts 'target/*.war'

                // Run the tests
                // sh 'mvn test'
              
            }
            // TODO stop if test fails!
            //post {
                //always {
                    // Record the test report?
                    // TO-DO
                //}
            //}
        }
        stage('Image creation') {
            agent any
            options {
                // Already compiled the WAR, so don't checkout againg (checkout also cleans the workspace, removing any generated artifact)
                skipDefaultCheckout true
            }
            steps {
                // The Dockerfile.artifact copies the code into the image and run the jar generation.
                echo 'Creating the image...'

                // This will search for a Dockerfile.artifact in the working directory and build the image to the local repository
                sh "docker build -t \"ditas/decision-system-for-data-and-computation-movement\" -f Dockerfile.artifact ."
                echo "Done"

                echo 'Retrieving Docker Hub password from /opt/ditas-docker-hub.passwd...'
                // Get the password from a file. This reads the file from the host, not the container. Slaves already have the password in there.
                script {
                    password = readFile '/opt/ditas-docker-hub.passwd'
                }
                echo "Done"

                echo 'Login to Docker Hub as ditasgeneric...'
                sh "docker login -u ditasgeneric -p ${password}"
                echo "Done"

                echo "Pushing the image ditas/decision-system-for-data-and-computation-movement:latest..."
                sh "docker push ditas/decision-system-for-data-and-computation-movement:latest"
                echo "Done "
            }
        }
        stage('Image deploy') {
            agent any
            options {
                // Don't need to checkout Git again
                skipDefaultCheckout true
            }
            steps {
                // Deploy to Staging environment calling the deployment script
                sh './jenkins/deploy-staging.sh'
            }
        }
    }
}
