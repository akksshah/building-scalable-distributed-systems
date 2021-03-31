if test -n "$1"; then
    echo "hello $1"
    cd ~/Desktop/bsds-clean/building-scalable-distributed-systems/clientPart2/ || exit
    echo "PACKAGING JAR FILE FOR DEPLOY"
    mvn package
    cd ../../
    echo "COPYING FILE TO CLIENT EC2"
    ssh-keyscan -H "$1" >> ~/.ssh/known_hosts
    echo "We assume that there is a directory named aakash already installed in the ec2"
    scp -i "aakash6650.pem" building-scalable-distributed-systems/clientPart2/out/artifacts/clientPart2_jar/clientPart2.jar ec2-user@"$1":aakash/client.jar
    echo "yes"
    ssh -i "aakash6650.pem" ec2-user@"$1"
else
  echo "USAGE ./install.sh <ec2 domain>"
fi
