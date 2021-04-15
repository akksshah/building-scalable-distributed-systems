echo "Enter ip for store consumer"
read -r ip
cd ~/Desktop/bsds-clean/building-scalable-distributed-systems/RabbitMqConsumer/ || exit
echo "==============================================================================="
echo "PACKAGING JAR FILE FOR DEPLOY"
mvn package
cd ../../
echo "COPYING FILE TO CLIENT EC2"
ssh-keyscan -H "$ip" >>~/.ssh/known_hosts
echo "We assume that there is a directory named aakash already installed in the ec2"
scp -i "aakash6650.pem" building-scalable-distributed-systems/RabbitMqConsumer/target/RabbitMqPurchaseConsumer-1.0-jar-with-dependencies.jar ec2-user@"$ip":aakash/client.jar
echo "run this >> java -jar aakash/client.jar"
ssh -i "aakash6650.pem" ec2-user@"$ip"
