# Staging environment: 31.171.247.162
# Private key for ssh: /opt/keypairs/ditas-testbed-keypair.pem

# TODO state management? We are killing without careing about any operation the conainer could be doing.

ssh -i /opt/keypairs/ditas-testbed-keypair.pem cloudsigma@31.171.247.162 << 'ENDSSH'
# Ensure that a previously running instance is stopped (-f stops and removes in a single step)
# || true - "docker stop" failt with exit status 1 if image doen't exists, what makes the Pipeline fail. the "|| true" forces the command to exit with 0.
sudo docker rm -f decision-system-for-data-and-computation-movement || true
sudo docker pull ditas/decision-system-for-data-and-computation-movement:staging
sudo docker run -p 30003:30003 --restart unless-stopped -d --name decision-system-for-data-and-computation-movement ditas/decision-system-for-data-and-computation-movement:staging
ENDSSH
