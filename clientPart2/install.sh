echo "Enter ip for client"
read -r ip
cd ~/Desktop/bsds-clean/building-scalable-distributed-systems/clientPart2/ || exit
read -r -p "Package and upload? [y/N] " yes
case "$yes" in
    [yY][eE][sS]|[yY])
        echo "==============================================================================="
        echo "PACKAGING JAR FILE FOR DEPLOY"
        mvn package || exit
        cd ../../
        echo "COPYING FILE TO CLIENT EC2"
        ssh-keyscan -H "$ip" >>~/.ssh/known_hosts
        echo "We assume that there is a directory named aakash already installed in the ec2"
        scp -i "aakash6650.pem" building-scalable-distributed-systems/clientPart2/target/clientPart2-1.0-jar-with-dependencies.jar ec2-user@"$ip":aakash/client.jar
echo "run this >> java -jar aakash/client.jar"
    ;;
esac
ssh -i "aakash6650.pem" ec2-user@"$ip"
read -r -p "Copy csv file? [y/N] " response
case "$response" in
    [yY][eE][sS]|[yY])
        read -r dir
        cd ~/Desktop/bsds-clean/building-scalable-distributed-systems/clientPart2/src/run_logs/ || exit
        mkdir "$dir"
        cd ~/Desktop/bsds-clean/ || exit
        scp -i "aakash6650.pem" ec2-user@"$ip":256Threads.csv "building-scalable-distributed-systems/clientPart2/src/run_logs/$dir/256Threads.csv"
        ;;
    *)
esac
